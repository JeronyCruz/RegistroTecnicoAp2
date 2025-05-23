package edu.ucne.registrotecnico.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TecnicosViewModel(
    private val tecnicosRepository: TecnicosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TecnicoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTecnicos()
    }

    fun onEvent(event: TecnicoEvent) {
        when (event) {
            TecnicoEvent.Delete -> TODO()
            TecnicoEvent.New -> TODO()
            is TecnicoEvent.NombreChange -> TODO()
            TecnicoEvent.Save -> TODO()
            is TecnicoEvent.SueldoChange -> TODO()
            is TecnicoEvent.TecnicoChange -> TODO()
        }

    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                tecnicoId = null,
                nombre = "",
                sueldo = 0.0,
                errorMessage = null
            )
        }
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicosRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnico = tecnicos)
                }
            }
        }
    }

    fun findTecnico(tecnicoId: Int) {
        viewModelScope.launch {
            if (tecnicoId > 0) {
                val tecnico = tecnicosRepository.find(tecnicoId)
                _uiState.update {
                    it.copy(
                        tecnicoId = tecnico?.tecnicoId,
                        nombre = tecnico?.nombre ?: "",
                        sueldo = tecnico?.sueldo ?: 0.0
                    )
                }
            }
        }
    }

//    fun saveTecnico(tecnico: TecnicoEntity) {
//        viewModelScope.launch {
//            tecnicosRepository.save(tecnico)
//        }
//    }

    private fun saveTecnico() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isNullOrBlank() && _uiState.value.sueldo > 0) {
                _uiState.update {
                    it.copy(errorMessage = "Campos vacios")
                }
            } else {
                tecnicosRepository.save(_uiState.value.toEntity())
            }
        }
    }

//    suspend fun findTecnico(id: Int): TecnicoEntity? {
//        return tecnicosRepository.find(id)
//    }

//    fun deleteTecnico(tecnico: TecnicoEntity) {
//        viewModelScope.launch {
//            tecnicosRepository.delete(tecnico)
//        }
//    }

    private fun deleteTecnico() {
        viewModelScope.launch {
            tecnicosRepository.delete(_uiState.value.toEntity())
        }
    }

//    val tecnicos: StateFlow<List<TecnicoEntity>> = tecnicosRepository.getAll()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )

    private fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre)
        }
    }

    private fun onSueldoChange(sueldo: Double) {
        _uiState.update {
            it.copy(sueldo = sueldo)
        }
    }

    private fun onTecnicoIdChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }
}

fun TecnicoUiState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombre = nombre ?: "",
    sueldo = sueldo ?: 0.0
)