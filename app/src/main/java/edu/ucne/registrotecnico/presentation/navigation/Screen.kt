package edu.ucne.registrotecnico.presentation.navigation
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object TecnicoList : Screen()
    @Serializable
    data class Tecnico(val tecnicoId: Int?) : Screen()

    @Serializable
    data object PrioridadList : Screen()
    @Serializable
    data class Prioridad(val priodidadId: Int?) : Screen()

    @Serializable
    data object TicketList : Screen()
    @Serializable
    data class Ticket(val ticketId: Int?) : Screen()

    @Serializable
    data object  Home : Screen()
}