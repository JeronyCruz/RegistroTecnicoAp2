package edu.ucne.registrotecnico.presentation.tickets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import java.util.Date


@Composable
fun TicketScreen(
    ticketId: Int? = null,
    viewModel: TicketsViewModel,
    navController: NavController,
    function: () -> Unit
) {
    // Estados para todos los campos del ticket
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(Date()) }
    var prioridadId by remember { mutableStateOf(1) } // Valor por defecto
    var tecnicoId by remember { mutableStateOf(1) } // Valor por defecto
    var editingTicket by remember { mutableStateOf<TicketEntity?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Estados para los dropdowns
    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedTecnico by remember { mutableStateOf(false) }

    // Suponiendo que tienes estas listas en tu ViewModel
    val prioridades by viewModel.prioridades.collectAsState()
    val tecnicos by viewModel.tecnicos.collectAsState()

    LaunchedEffect(ticketId) {
        if (ticketId != null && ticketId > 0) {
            val ticket = viewModel.findTicket(ticketId)
            ticket?.let {
                editingTicket = it
                asunto = it.asunto
                descripcion = it.descripcion
                cliente = it.cliente
                fecha = it.fecha
                prioridadId = it.prioridadId
                tecnicoId = it.tecnicoId
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
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (editingTicket == null) "Nuevo Ticket" else "Editar Ticket",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }

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
                    // Campo ID (solo lectura)
                    OutlinedTextField(
                        value = editingTicket?.ticketId?.toString() ?: "Nuevo",
                        onValueChange = {},
                        label = { Text("ID del Ticket") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Asunto
                    OutlinedTextField(
                        value = asunto,
                        onValueChange = { asunto = it },
                        label = { Text("Asunto") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Color.Green,
                            unfocusedBorderColor = Color.Black,
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Descripción
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5,
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Color.Green,
                            unfocusedBorderColor = Color.Black,
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Cliente
                    OutlinedTextField(
                        value = cliente,
                        onValueChange = { cliente = it },
                        label = { Text("Cliente") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Color.Green,
                            unfocusedBorderColor = Color.Black,
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Selector de Prioridad
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = prioridades.find { it.prioridadId == prioridadId }?.descripcion ?: "Seleccione técnico",
                            onValueChange = {},
                            label = { Text("Prioridad") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Color.Green,
                                unfocusedBorderColor = Color.Black,
                            )
                        )
                        DropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            prioridades.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = { Text(prioridad.descripcion) },
                                    onClick = {
                                        prioridadId = prioridad.prioridadId ?: 0 // ← Corrección aquí (elimina el punto)
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Selector de Técnico
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = tecnicos.find { it.tecnicoId == tecnicoId }?.nombre ?: "Seleccione técnico",
                            onValueChange = {},
                            label = { Text("Técnico Asignado") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Color.Green,
                                unfocusedBorderColor = Color.Black,
                            )
                        )
                        DropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            tecnicos.forEach { tecnico ->
                                DropdownMenuItem(
                                    text = {
                                        Text(tecnico.nombre)
                                    },
                                    onClick = {
                                        tecnicoId = tecnico.tecnicoId ?: 0
                                        expandedTecnico = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostrar mensajes de error
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Botón Limpiar
                        OutlinedButton(
                            onClick = {
                                asunto = ""
                                descripcion = ""
                                cliente = ""
                                prioridadId = 1
                                tecnicoId = 1
                                errorMessage = null
                                editingTicket = null
                            },
                            modifier = Modifier.padding(4.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.Blue),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Blue
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = Color.Blue)
                            Text(" Limpiar")
                        }

                        // Botón Guardar
                        OutlinedButton(
                            onClick = {
                                if (asunto.isBlank()) {
                                    errorMessage = "El asunto no puede estar vacío"
                                    return@OutlinedButton
                                }
                                if (descripcion.isBlank()) {
                                    errorMessage = "La descripción no puede estar vacía"
                                    return@OutlinedButton
                                }
                                if (cliente.isBlank()) {
                                    errorMessage = "El cliente no puede estar vacío"
                                    return@OutlinedButton
                                }

                                viewModel.saveTicket(
                                    TicketEntity(
                                        ticketId = editingTicket?.ticketId,
                                        asunto = asunto,
                                        descripcion = descripcion,
                                        cliente = cliente,
                                        fecha = fecha,
                                        prioridadId = prioridadId,
                                        tecnicoId = tecnicoId
                                    )
                                )

                                // Limpiar después de guardar
                                asunto = ""
                                descripcion = ""
                                cliente = ""
                                prioridadId = 1
                                tecnicoId = 1
                                errorMessage = null
                                editingTicket = null

                                navController.navigateUp()
                            },
                            modifier = Modifier.padding(4.dp),
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