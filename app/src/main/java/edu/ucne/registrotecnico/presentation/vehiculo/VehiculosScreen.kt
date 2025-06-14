package edu.ucne.registrotecnico.presentation.vehiculo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.presentation.UiEvent


@Composable
fun VehiculoScreen(
    vehiculoId: Int? = null,
    viewModel: VehiculoViewModel = hiltViewModel(),
//    navController: NavController,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vehiculoId) {
        vehiculoId?.let { id ->
            if (id > 0) {
                viewModel.findVehiculo(id)
            } else {
                viewModel.OnEvent(VehiculoEvent.Nuevo)
            }
        } ?: viewModel.OnEvent(VehiculoEvent.Nuevo)
    }

    VehiculoBodyScreen(
        uiState = uiState,
        onEvent = viewModel::OnEvent,
        goBack = goBack,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculoBodyScreen(
    uiState: VehiculoUiState,
    onEvent: (VehiculoEvent) -> Unit,
    goBack: () -> Unit,
    viewModel: VehiculoViewModel
) {
    // Paleta de colores personalizada
    val primaryColor = Color(0xFF272D4D)
    val secondaryColor = Color(0xFFB83564)
    val accentColor = Color(0xFFFF6A5A)
    val lightAccentColor = Color(0xFFFFB350)
    val complementaryColor = Color(0xFF83B8AA)

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateUp -> goBack()
                is UiEvent.ShowSnackbar -> TODO()
            }
        }
    }

    // Mostrar Snackbar cuando el estado cambie
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage) {
            snackbarHostState.showSnackbar(
                message = uiState.successMessage ?: "Operación exitosa",
                duration = SnackbarDuration.Short
            )
            onEvent(VehiculoEvent.ResetSuccessMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.vehiculoId != null && uiState.vehiculoId != 0)
                            "Editar Vehículo" else "Nuevo Vehículo",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            // Tarjeta contenedora
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Campo ID (solo lectura)
                    OutlinedTextField(
                        value = uiState.vehiculoId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Vehículo") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Descripción
                    OutlinedTextField(
                        value = uiState.descripcion,
                        onValueChange = { onEvent(VehiculoEvent.DescripcionChange(it)) },
                        label = { Text("Descripción del Vehículo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiState.descripcion.isNullOrBlank() && !uiState.errorDescripcion.isNullOrBlank(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            errorBorderColor = secondaryColor
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Descripción",
                                tint = primaryColor
                            )
                        },
                        supportingText = {
                            uiState.errorDescripcion?.let { error ->
                                Text(
                                    text = error,
                                    color = secondaryColor
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Precio
                    OutlinedTextField(
                        value = uiState.precio.toString(),
                        onValueChange = {
                            onEvent(VehiculoEvent.PrecioChange(it.toDoubleOrNull() ?: 0.0))
                        },
                        label = { Text("Precio ($)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = (uiState.precio <= 0) && !uiState.errorPrecio.isNullOrBlank(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            errorBorderColor = secondaryColor
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Precio",
                                tint = primaryColor
                            )
                        },
                        supportingText = {
                            uiState.errorPrecio?.let { error ->
                                Text(
                                    text = error,
                                    color = secondaryColor
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { onEvent(VehiculoEvent.Nuevo) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = complementaryColor,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Limpiar")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Limpiar")
                        }

                        Button(
                            onClick = {
                                onEvent(VehiculoEvent.PostVehiculo)
                                focusManager.clearFocus()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accentColor,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardar")
                        }
                    }
                }
            }

            // Mensaje de error general
            if (!uiState.errorMessage.isNullOrBlank()) {
                Card(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = secondaryColor.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, secondaryColor.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = secondaryColor,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = uiState.errorMessage!!,
                            color = secondaryColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}