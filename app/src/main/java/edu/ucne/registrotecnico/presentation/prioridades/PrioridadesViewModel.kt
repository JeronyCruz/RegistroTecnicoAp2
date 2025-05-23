package edu.ucne.registrotecnico.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.PrioridadesRepository
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import edu.ucne.registrotecnico.presentation.tecnicos.TecnicoUiState
import edu.ucne.registrotecnico.presentation.tecnicos.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrioridadesViewModel (
    private val prioridadRepository: PrioridadesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun  onEvent(event: PrioridadEvent){
        when(event){
            PrioridadEvent.Delete -> TODO()
            is PrioridadEvent.DescripcionChange -> TODO()
            PrioridadEvent.New -> TODO()
            is PrioridadEvent.PrioridadChange -> TODO()
            PrioridadEvent.Save -> TODO()
        }
    }

//    fun savePrioridad(prioridad: PrioridadEntity) {
//        viewModelScope.launch {
//            prioridadRepository.save(prioridad)
//        }
//    }
//
//    suspend fun findPrioridad(id: Int): PrioridadEntity? {
//        return prioridadRepository.find(id)
//    }
//
//    fun deletePrioridad(prioridad: PrioridadEntity) {
//        viewModelScope.launch {
//            prioridadRepository.delete(prioridad)
//        }
//    }

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

    private fun savePrioridad() {
        viewModelScope.launch {
            if (_uiState.value.descripcion.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "Campos vacios")
                }
            } else {
                prioridadRepository.save(_uiState.value.toEntity())
            }
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

//    // Exponer las prioridades como StateFlow
//    val prioridades: StateFlow<List<PrioridadEntity>> = prioridadRepository.getAll()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )

}

fun PrioridadUiState.toEntity() = PrioridadEntity(
    prioridadId = prioridadId,
    descripcion = descripcion ?: "",
)