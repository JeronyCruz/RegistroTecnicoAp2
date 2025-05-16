package edu.ucne.registrotecnico.presentation.tecnicos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.saveTecnico
import kotlinx.coroutines.launch

@Composable
fun TecnicoScreen(
    tecniId: Int? = null,
    viewModel: TecnicosViewModel,
    navController: NavController,
    function: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var sueldo by remember { mutableStateOf(0.0) }
    var editingTecnico by remember { mutableStateOf<TecnicoEntity?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(tecniId) {
        if (tecniId != null && tecniId > 0) {
            val tecnico = viewModel.findTecnico(tecniId)
            tecnico?.let {
                editingTecnico = it
                nombre = it.nombre
                sueldo = it.sueldo
            }
        }
    }


    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navController != null) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Registro de Técnicos",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center

                )
                }
            // Formulario de Registro
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = editingTecnico?.tecnicoId?.toString()
                            ?: "0", // <- Muestra el ID real
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

                                viewModel.saveTecnico(
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
                            },
                            modifier = Modifier
                                .padding(4.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.Blue)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Guardar",
                                tint = Color.Blue
                            )
                            Text(" Guardar")
                        }
                    }
                }
            }
        }

    }
}