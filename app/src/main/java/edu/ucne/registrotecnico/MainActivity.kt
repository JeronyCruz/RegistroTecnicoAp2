package edu.ucne.registrotecnico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create  // Para reemplazar Edit
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoAp2Theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration().build()

        setContent {
            RegistroTecnicoAp2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TecnicoScreen(
                            tecnicoDb = tecnicoDb,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TecnicoScreen(
    tecnicoDb: TecnicoDb,
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var sueldo by remember { mutableStateOf(0.0) }
    var editingTecnico by remember { mutableStateOf<TecnicoEntity?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val tecnicoList by tecnicoDb.TecnicoDao().getAll()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column(modifier = modifier.fillMaxSize()) {
        // Formulario de Registro
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editingTecnico?.tecnicoId?.toString() ?: "0", // <- Muestra el ID real
                    onValueChange = {},
                    label = { Text("ID Técnico") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = false
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Técnico") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Black,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = sueldo.toString(),
                    onValueChange = { sueldo = it.toDoubleOrNull() ?: 0.0 },
                    label = { Text("Sueldo ($)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Black,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            nombre = ""
                            sueldo = 0.0
                            errorMessage = null
                            editingTecnico = null
                        },
                        modifier = Modifier
                            .padding(4.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.Blue),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Blue
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = Color.Blue)
                        Text(" Limpiar")
                    }

                    OutlinedButton(
                        onClick = {
                            if (nombre.isBlank()) {
                                errorMessage = "El nombre no puede estar vacío"
                                return@OutlinedButton
                            }
                            if (sueldo <= 0) {
                                errorMessage = "El sueldo debe ser mayor a 0"
                                return@OutlinedButton
                            }

                            scope.launch {
                                saveTecnico(
                                    tecnicoDb,
                                    TecnicoEntity(
                                        tecnicoId = editingTecnico?.tecnicoId,
                                        nombre = nombre,
                                        sueldo = sueldo
                                    )
                                )
                                nombre = ""
                                sueldo = 0.0
                                errorMessage = null
                                editingTecnico = null
                            }
                        },
                        modifier = Modifier
                            .padding(4.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.Blue)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Guardar", tint = Color.Blue)
                        Text(" Guardar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TecnicoList(
            tecnicoList = tecnicoList,
            onEditClick = { tecnico ->
                nombre = tecnico.nombre
                sueldo = tecnico.sueldo
                editingTecnico = tecnico
            },
            onDeleteClick = { tecnico ->
                scope.launch {
                    tecnicoDb.TecnicoDao().delete(tecnico)
                }
            }
        )


    }
}

@Composable
fun TecnicoList(
    tecnicoList: List<TecnicoEntity>,
    onEditClick: (TecnicoEntity) -> Unit,
    onDeleteClick: (TecnicoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Técnicos Registrados",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
        ) {
            items(tecnicoList) { tecnico ->
                TecnicoCard(
                    tecnico = tecnico,
                    onEditClick = { onEditClick(tecnico) },
                    onDeleteClick = { onDeleteClick(tecnico) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun TecnicoCard(
    tecnico: TecnicoEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ID: ${tecnico.tecnicoId}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = tecnico.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = "$${"%.2f".format(tecnico.sueldo)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            IconButton(onClick = onEditClick) {
                Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Color.Green)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = Color.Green)
            }
        }
    }
}

suspend fun saveTecnico(tecnicoDb: TecnicoDb, tecnico: TecnicoEntity){
    tecnicoDb.TecnicoDao().save(tecnico)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RegistroTecnicoAp2Theme {
        Greeting("Android")
    }
}

