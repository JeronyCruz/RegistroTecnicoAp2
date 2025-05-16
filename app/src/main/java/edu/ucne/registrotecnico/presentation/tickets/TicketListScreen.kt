package edu.ucne.registrotecnico.presentation.tickets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoAp2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity>,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: (TicketEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tickets") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEditClick(0) }) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(ticketList) { ticket ->
                    TicketCard(
                        ticket = ticket,
                        onEditClick = { onEditClick(ticket.ticketId) },
                        onDeleteClick = { onDeleteClick(ticket) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
fun TicketCard(
    ticket: TicketEntity,
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
                    text = "ID: ${ticket.ticketId}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = ticket.descripcion,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Color.Green)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = Color.Green)
            }
        }
    }
}

//@Preview
//@Composable
//private fun Preview() {
//    val ticket = listOf(
//        TicketEntity(
//            ticketId = 1,
//            descripcion = "Alta",
//        ),
//        TicketEntity(
//            ticketId = 2,
//            descripcion = "Baja",
//        )
//    )
//    RegistroTecnicoAp2Theme {
//        TicketListScreen(
//            ticketList = ticket,
//            onEditClick = {},
//            onDeleteClick = {}
//        )
//    }
//}