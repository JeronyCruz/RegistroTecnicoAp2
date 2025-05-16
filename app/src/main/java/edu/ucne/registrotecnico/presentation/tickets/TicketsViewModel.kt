package edu.ucne.registrotecnico.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.data.repository.PrioridadesRepository
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import edu.ucne.registrotecnico.data.repository.TicketsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketsViewModel (
    private val ticketsRepository: TicketsRepository,
    private val tecnicosRepository: TecnicosRepository,
    private val prioridadesRepository: PrioridadesRepository
): ViewModel() {
    private val _tickets = MutableStateFlow<List<TicketEntity>>(emptyList())
    val tickets: StateFlow<List<TicketEntity>> = _tickets.asStateFlow()

    // Estado para las prioridades
    val prioridades = prioridadesRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Para Técnicos
    val tecnicos = tecnicosRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        getAllTickets()
    }

    fun getAllTickets() {
        ticketsRepository.getAll()
            .onEach { tickets ->
                _tickets.value = tickets
            }
            .launchIn(viewModelScope)
    }
    fun saveTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketsRepository.save(ticket)
        }
    }

    suspend fun findTicket(id: Int): TicketEntity? {
        return ticketsRepository.find(id)
    }

    fun deleteTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketsRepository.delete(ticket)
        }
    }
}