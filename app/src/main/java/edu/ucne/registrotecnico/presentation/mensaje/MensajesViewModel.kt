package edu.ucne.registrotecnico.presentation.mensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.repository.MensajesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class MensajesViewModel @Inject constructor(
    private val mensajeRepository: MensajesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MensajeUiState(
        ticketId = 0
    ))
    val uiState = _uiState.asStateFlow()

//    init {
//        getMensajes()
//    }

    fun onEvent(event: MensajeEvent) {
        when (event) {
            is MensajeEvent.ContenidoChange -> onContenidoChange(event.contenido)
            MensajeEvent.Delete -> deleteMensaje()
            is MensajeEvent.FechaChange -> onFechaChange(event.fecha)
            is MensajeEvent.MensajeChange -> onMensajeIdChange(event.mensajeId)
            MensajeEvent.New -> nuevo()
            is MensajeEvent.RemitenteChange -> onRemitenteChange(event.remitente)
            MensajeEvent.Save -> saveMensaje()
            is MensajeEvent.TicketIdChange -> {
                onTicketIdChange(event.ticketId)
                getMensajes(event.ticketId)
            }

            is MensajeEvent.tipoRemitenteChange -> onttipoRemitenteChange(event.tipoRemitente)
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                mensajeId = null,
                fecha = Date(),
                contenido = "",
                remitente = "",
                ticketId = 0,
                errorMessage = null
            )
        }
    }

    fun getMensajes(ticketId: Int) {
        viewModelScope.launch {
            mensajeRepository.getAll(ticketId).collect() { mensajes ->
                _uiState.update {
                    it.copy(mensajes = mensajes)
                }
            }
        }
    }

    fun findPrioridad(mensajeId: Int) {
        viewModelScope.launch {
            if (mensajeId > 0) {
                val mensaje = mensajeRepository.find(mensajeId)
                _uiState.update {
                    it.copy(
                        mensajeId = mensaje?.mensajeId,
                        fecha = mensaje?.fecha ?: Date(),
                        contenido = mensaje?.contenido ?: "",
                        remitente = mensaje?.remitente ?: "",
                        ticketId = mensaje?.ticketId ?: 0,
                    )
                }
            }
        }
    }

    private fun saveMensaje() {
        viewModelScope.launch {
            if (_uiState.value.contenido.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "Campos vacios")
                }
            } else {
                mensajeRepository.save(_uiState.value.toEntity())
                _uiState.update {
                    it.copy(
                        contenido = "",
                        remitente = "",
                        errorMessage = null
                    )
                }
            }
        }
    }


    private fun deleteMensaje() {
        viewModelScope.launch {
            mensajeRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun onFechaChange(fecha: Date) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    private fun onMensajeIdChange(mensajeId: Int) {
        _uiState.update {
            it.copy(mensajeId = mensajeId)
        }
    }

    private fun onContenidoChange(contenido: String) {
        _uiState.update {
            it.copy(contenido = contenido)
        }
    }

    private fun onRemitenteChange(remitente: String) {
        _uiState.update {
            it.copy(remitente = remitente)
        }
    }

    private fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
    }
    private fun onttipoRemitenteChange(tipoRemitente: String) {
        _uiState.update {
            it.copy(tipoRemitente = tipoRemitente)
        }
    }
}

fun MensajeUiState.toEntity() = MensajeEntity(
    mensajeId = mensajeId,
    fecha = fecha ?: Date(),
    contenido = contenido ?: "",
    remitente = remitente ?: "",
    tipoRemitente = tipoRemitente ?: "",
    ticketId = ticketId

    )

