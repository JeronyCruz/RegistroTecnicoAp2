package edu.ucne.registrotecnico.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.PrioridadesRepository
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import kotlinx.coroutines.launch

class PrioridadesViewModel (
    private val prioridadRepository: PrioridadesRepository
): ViewModel() {
    fun savePrioridad(prioridad: PrioridadEntity) {
        viewModelScope.launch {
            prioridadRepository.save(prioridad)
        }
    }

    suspend fun findPrioridad(id: Int): PrioridadEntity? {
        return prioridadRepository.find(id)
    }

    fun deletePrioridad(prioridad: PrioridadEntity) {
        viewModelScope.launch {
            prioridadRepository.delete(prioridad)
        }
    }

}