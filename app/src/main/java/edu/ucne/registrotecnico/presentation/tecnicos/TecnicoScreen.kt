package edu.ucne.registrotecnico.presentation.tecnicos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun TecnicoScreen(
    tecnicoId: Int? = null,
    viewModel: TecnicosViewModel = hiltViewModel(),
//    navController: NavController,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(tecnicoId) {
        tecnicoId?.let {
            if (it > 0) {
                viewModel.findTecnico(it)
            }
        }
    }

    TecnicoBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoBodyScreen(
    uiState: TecnicoUiState,
    onEvent: (TecnicoEvent) -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.tecnicoId != null && uiState.tecnicoId != 0) "Editar Técnico" else "Nuevo Técnico",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Campo ID (solo lectura)
            OutlinedTextField(
                value = uiState.tecnicoId?.toString() ?: "Nuevo",
                onValueChange = {},
                label = { Text("ID Técnico") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nombre
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { onEvent(TecnicoEvent.NombreChange(it)) },
                label = { Text("Nombre del Técnico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Sueldo
            OutlinedTextField(
                value = uiState.sueldo.toString(),
                onValueChange = {
                    onEvent(TecnicoEvent.SueldoChange(it.toDoubleOrNull() ?: 0.0))
                },
                label = { Text("Sueldo ($)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage != null
            )

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onEvent(TecnicoEvent.New) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.secondaryContainer,
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Limpiar")
                }

                Button(
                    onClick = {
                        onEvent(TecnicoEvent.Save)
                        goBack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Guardar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar")
                }
            }
        }
    }
}