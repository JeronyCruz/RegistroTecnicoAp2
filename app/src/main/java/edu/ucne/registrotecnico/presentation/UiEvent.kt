package edu.ucne.registrotecnico.presentation

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object NavigateUp : UiEvent()
}