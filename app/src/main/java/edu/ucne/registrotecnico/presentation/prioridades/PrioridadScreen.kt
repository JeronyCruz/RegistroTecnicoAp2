package edu.ucne.registrotecnico.presentation.prioridades

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicosViewModel

@Composable
fun PrioridadScreen(
    prioridadId: Int? = null,
    viewModel: PrioridadesViewModel,
    navController: NavController,
    function: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var editingPrioridad by remember { mutableStateOf<PrioridadEntity?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(prioridadId) {
        if (prioridadId != null && prioridadId > 0) {
            val prioridad = viewModel.findPrioridad(prioridadId)
            prioridad?.let {
                editingPrioridad = it
                descripcion = it.descripcion
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
                    text = "Registro de Prioridades",
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
                        value = editingPrioridad?.prioridadId?.toString()
                            ?: "0", // <- Muestra el ID real
                        onValueChange = {},
                        label = { Text("ID Prioridad") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripcion de Prioridad") },
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
                                descripcion = ""
                                errorMessage = null
                                editingPrioridad = null
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
                                if (descripcion.isBlank()) {
                                    errorMessage = "El nombre no puede estar vacío"
                                    return@OutlinedButton
                                }

                                viewModel.savePrioridad(
                                    PrioridadEntity(
                                        prioridadId = editingPrioridad?.prioridadId,
                                        descripcion = descripcion,
                                    )
                                )
                                descripcion = ""
                                errorMessage = null
                                editingPrioridad = null

                                navController.navigateUp()
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