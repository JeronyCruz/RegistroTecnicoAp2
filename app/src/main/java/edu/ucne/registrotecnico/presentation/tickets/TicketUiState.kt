package edu.ucne.registrotecnico.presentation.tickets

import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import java.util.Date

data class TicketUiState(
    val ticketId: Int? = null,
    val fecha: Date = Date(),
    val prioridadId: Int,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int,
    val errorMessage: String? = null,
    val tickets: List<TicketEntity> = emptyList(),
    val prioridades: List<PrioridadEntity> = emptyList(),  // Añade esto
    val tecnicos: List<TecnicoEntity> = emptyList()
)