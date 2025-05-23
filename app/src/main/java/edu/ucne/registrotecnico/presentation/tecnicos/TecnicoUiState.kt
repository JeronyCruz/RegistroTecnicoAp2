package edu.ucne.registrotecnico.presentation.tecnicos

import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity

data class TecnicoUiState(
    val tecnicoId: Int? = null,
    val nombre: String = "",
    val sueldo: Double = 0.0,
    val errorMessage: String? = null,
    val tecnico: List<TecnicoEntity> = emptyList()
)