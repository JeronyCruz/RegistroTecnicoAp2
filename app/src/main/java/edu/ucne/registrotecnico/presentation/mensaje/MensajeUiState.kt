package edu.ucne.registrotecnico.presentation.mensaje

import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import java.util.Date

data class MensajeUiState (
    val mensajeId: Int? = null,
    val fecha: Date = Date(),
    val contenido: String = "",
    val remitente: String = "",
    val ticketId: Int,
    val errorMessage: String? = null,
    val mensajes: List<MensajeEntity> = emptyList()
)