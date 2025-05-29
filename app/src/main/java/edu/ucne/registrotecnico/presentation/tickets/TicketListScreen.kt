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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketListScreen(
    viewModel: TicketsViewModel = hiltViewModel(),
    goToTicket: (Int) -> Unit,
    goToMessages: (Int) -> Unit, // Nuevo parámetro para navegar a mensajes
    createTicket: () -> Unit,
    deleteTicket: ((TicketEntity) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    TicketListBodyScreen(
        uiState = uiState,
        goToTicket = goToTicket,
        createTicket = createTicket,
        deleteTicket = { ticket ->
            viewModel.onEvent(TicketEvent.TicketChange(ticket.ticketId ?: 0))
            viewModel.onEvent(TicketEvent.Delete)
        },
        onMessageClick = goToMessages // Pasamos la función de navegación
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListBodyScreen(
    uiState: TicketUiState,
    goToTicket: (Int) -> Unit,
    createTicket: () -> Unit,
    deleteTicket: (TicketEntity) -> Unit,
    onMessageClick: (Int) -> Unit // Nuevo parámetro para manejar la navegación a mensajes
) {
    // Definición de colores personalizados
    val primaryColor = Color(0xFF272D4D)
    val secondaryColor = Color(0xFFB83564)
    val complementaryColor = Color(0xFF83B8AA)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Tickets",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTicket,
                containerColor = secondaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Agregar nuevo ticket")
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            // Card contenedor con mejor diseño
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                if (uiState.tickets.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay tickets registrados",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(uiState.tickets) { ticket ->
                            TicketCard(
                                ticket = ticket,
                                goToTicket = { goToTicket(ticket.ticketId ?: 0) },
                                deleteTicket = deleteTicket,
                                primaryColor = primaryColor,
                                secondaryColor = secondaryColor,
                                complementaryColor = complementaryColor,
                                onMessageClick = { onMessageClick(ticket.ticketId ?: 0) } // Pasamos el ticketId
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketCard(
    ticket: TicketEntity,
    goToTicket: () -> Unit,
    deleteTicket: (TicketEntity) -> Unit,
    primaryColor: Color,
    secondaryColor: Color,
    complementaryColor: Color,
    onMessageClick: () -> Unit // Nuevo parámetro para manejar el clic en el icono de mensaje
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ID del ticket
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Ticket #${ticket.ticketId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                // Fecha
                Text(
                    text = "${SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault()).format(ticket.fecha)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cliente
            Text(
                text = "Cliente: ${ticket.cliente ?: "Sin Cliente"}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Asunto
            Text(
                text = ticket.asunto ?: "Sin asunto",
                style = MaterialTheme.typography.bodySmall.copy(
//                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fila de acciones - Agregamos el nuevo icono de mensaje primero
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Icono de mensaje/comentario
                IconButton(
                    onClick = onMessageClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Mensajes",
                        tint = complementaryColor
                    )
                }

                // Icono de edición
                IconButton(
                    onClick = goToTicket,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Editar",
                        tint = primaryColor
                    )
                }

                // Icono de eliminación
                IconButton(
                    onClick = { deleteTicket(ticket) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = "Eliminar",
                        tint = secondaryColor
                    )
                }
            }
        }
    }
}

fun Date.toFormattedString(): String {
    val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return format.format(this)
}

//@Preview
//@Composable
//private fun Preview() {
//    val tickets = listOf(
//        TicketEntity(
//            ticketId = 1,
//            asunto = "Problema con la red",
//            fecha = Date(),
//            descripcion = "No puedo conectarme a la red WiFi"
//        ),
//        TicketEntity(
//            ticketId = 2,
//            asunto = "Software no funciona",
//            fecha = Date(),
//            descripcion = "El programa XYZ no se inicia"
//        ),
//        TicketEntity(
//            ticketId = 3,
//            asunto = "Solicitud de hardware",
//            fecha = Date(),
//            descripcion = "Necesito un nuevo teclado"
//        )
//    )
//
//    val mockUiState = TicketUiState(
//        tickets = tickets
//    )
//
//    MaterialTheme {
//        TicketListBodyScreen(
//            uiState = mockUiState,
//            goToTicket = {},
//            createTicket = {},
//            deleteTicket = {}
//        )
//    }
//}