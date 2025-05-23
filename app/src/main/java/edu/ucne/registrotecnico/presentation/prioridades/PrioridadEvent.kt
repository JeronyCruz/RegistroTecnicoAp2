package edu.ucne.registrotecnico.presentation.prioridades

import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoEvent

sealed interface PrioridadEvent {
    data class PrioridadChange(val prioridadId: Int): PrioridadEvent
    data class DescripcionChange(val descripcion:String): PrioridadEvent
    data object Save: PrioridadEvent
    data object Delete: PrioridadEvent
    data object New: PrioridadEvent
}