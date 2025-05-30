package edu.ucne.registrotecnico.presentation.tickets

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
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch


@Composable
fun TicketScreen(
    ticketId: Int? = null,
    viewModel: TicketsViewModel = hiltViewModel(),
//    navController: NavController,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(ticketId) {
        ticketId?.let {
            if (it > 0) {
                viewModel.findTickets(it)
            }
        }
    }

    TicketBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    uiState: TicketUiState,
    onEvent: (TicketEvent) -> Unit,
    goBack: () -> Unit,
    viewModel: TicketsViewModel
) {
    val scope = rememberCoroutineScope()
    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedTecnico by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.ticketId != null && uiState.ticketId != 0) "Editar Ticket" else "Nuevo Ticket",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Campo ID (solo lectura)
                OutlinedTextField(
                    value = uiState.ticketId?.toString() ?: "0",
                    onValueChange = {},
                    label = { Text("ID Ticket") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Asunto
                OutlinedTextField(
                    value = uiState.asunto,
                    onValueChange = { onEvent(TicketEvent.AsuntoChange(it)) },
                    label = { Text("Asunto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Descripción
                OutlinedTextField(
                    value = uiState.descripcion,
                    onValueChange = { onEvent(TicketEvent.DescripcionChange(it)) },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    isError = uiState.errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Cliente
                OutlinedTextField(
                    value = uiState.cliente,
                    onValueChange = { onEvent(TicketEvent.ClienteChange(it)) },
                    label = { Text("Cliente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown Prioridad
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expandedPrioridad = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            uiState.prioridades.find { it.prioridadId == uiState.prioridadId }?.descripcion
                                ?: "Seleccione prioridad",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Prioridad")
                    }
                    DropdownMenu(
                        expanded = expandedPrioridad,
                        onDismissRequest = { expandedPrioridad = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.prioridades.forEach { prioridad ->
                            DropdownMenuItem(
                                text = { Text(prioridad.descripcion) },
                                onClick = {
                                    onEvent(
                                        TicketEvent.PrioridadIdChange(
                                            prioridad.prioridadId ?: 0
                                        )
                                    )
                                    expandedPrioridad = false
                                }
                            )
                        }
                    }
                }

                if (uiState.errorMessage != null && uiState.prioridadId == 0) {
                    Text(
                        text = "Seleccione una prioridad",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown Técnico
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expandedTecnico = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            uiState.tecnicos.find { it.tecnicoId == uiState.tecnicoId }?.nombre
                                ?: "Seleccione técnico",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Técnico")
                    }
                    DropdownMenu(
                        expanded = expandedTecnico,
                        onDismissRequest = { expandedTecnico = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.tecnicos.forEach { tecnico ->
                            DropdownMenuItem(
                                text = { Text(tecnico.nombre) },
                                onClick = {
                                    onEvent(TicketEvent.TecnicoIdChange(tecnico.tecnicoId ?: 0))
                                    expandedTecnico = false
                                }
                            )
                        }
                    }
                }

                if (uiState.errorMessage != null && uiState.tecnicoId == 0) {
                    Text(
                        text = "Seleccione un técnico",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onEvent(TicketEvent.New) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Limpiar")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                val result = viewModel.saveTicket()
                                if (result) {
                                    goBack()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Guardar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar")
                    }
                }
            }
        }
    }
}


