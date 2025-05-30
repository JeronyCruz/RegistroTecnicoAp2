package edu.ucne.registrotecnico.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.repository.PrioridadesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadesViewModel @Inject constructor(
    private val prioridadRepository: PrioridadesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun onEvent(event: PrioridadEvent) {
        when (event) {
            PrioridadEvent.Delete -> deletePrioridad()
            is PrioridadEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            PrioridadEvent.New -> nuevo()
            is PrioridadEvent.PrioridadChange -> onPrioridadIdChange(event.prioridadId)
            PrioridadEvent.Save -> viewModelScope.launch { savePrioridad() }
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                errorMessage = null
            )
        }
    }

    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getAll().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    fun findPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = prioridadRepository.find(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion ?: ""
                    )
                }
            }
        }
    }

    suspend fun savePrioridad(): Boolean {
        return if (_uiState.value.descripcion.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Campos vacios o Invalidos")
            }
            false
        } else {
            prioridadRepository.save(_uiState.value.toEntity())
            true
        }
    }


    private fun deletePrioridad() {
        viewModelScope.launch {
            prioridadRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

}

fun PrioridadUiState.toEntity() = PrioridadEntity(
    prioridadId = prioridadId,
    descripcion = descripcion ?: "",
)