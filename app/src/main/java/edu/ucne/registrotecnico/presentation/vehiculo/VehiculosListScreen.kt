package edu.ucne.registrotecnico.presentation.vehiculo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.data.remote.dto.VehiculoDto


@Composable
fun VehiculoListScreen(
    viewModel: VehiculoViewModel = hiltViewModel(),
    goToVehiculo: (Int) -> Unit,
    createVehiculo: () -> Unit,
    deleteVehiculo: ((VehiculoDto) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VehiculoListBodyScreen(
        uiState = uiState,
        goToVehiculo = { id ->
            viewModel.OnEvent(VehiculoEvent.VehiculoChange(id))
            goToVehiculo(id)
        },
        onEvent = viewModel::OnEvent,
        createVehiculo = createVehiculo,
//        deleteVehiculo = { vehiculo ->
//            viewModel.OnEvent(VehiculoEvent.VehiculoChange(vehiculo.vehiculoId ?: 0))
//            viewModel.OnEvent(VehiculoEvent)
//        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun VehiculoListBodyScreen(
    uiState: VehiculoUiState,
    goToVehiculo: (Int) -> Unit,
    onEvent: (VehiculoEvent) -> Unit,
    createVehiculo: () -> Unit,
) {
    // Definición de colores personalizados
    val primaryColor = Color(0xFF272D4D)
    val secondaryColor = Color(0xFFB83564)
    val accentColor = Color(0xFFFF6A5A)
    val lightAccentColor = Color(0xFFFFB350)
    val complementaryColor = Color(0xFF83B8AA)

    val refreshing = uiState.isLoading // Asume que VehiculoUiState tiene un campo isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { onEvent(VehiculoEvent.GetVehiculo) }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "API Vehiculos",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createVehiculo,
                containerColor = secondaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Agregar nuevo Vehiculo")
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    if (uiState.vehiculos.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay vehiculos registrados",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            items(uiState.vehiculos) { vehiculos ->
                                VehiculoCard(
                                    vehiculo = vehiculos,
                                    goToVehiculo = { goToVehiculo(vehiculos.vehiculoId ?: 0) },
                                    primaryColor = primaryColor,
                                    secondaryColor = secondaryColor
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = secondaryColor // Color del indicador de refresh
            )

            if (!uiState.errorMessage.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = secondaryColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun VehiculoCard(
    vehiculo: VehiculoDto,
    goToVehiculo: () -> Unit,
//    deleteTecnico: (TecnicoEntity) -> Unit,
    primaryColor: Color,
    secondaryColor: Color
) {
    val complementaryColor = Color(0xFF83B8AA)

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar/Iniciales
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = primaryColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = primaryColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = vehiculo.descripcion.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vehiculo.descripcion,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "ID: ${vehiculo.vehiculoId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = "$${"%.2f".format(vehiculo.precio)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = complementaryColor
            )

            Spacer(modifier = Modifier.width(8.dp))

//            IconButton(
//                onClick = goToVehiculo,
//                modifier = Modifier.size(48.dp)
//            ) {
//                Icon(
//                    Icons.Outlined.Edit,
//                    contentDescription = "Editar",
//                    tint = primaryColor
//                )
//            }

//            IconButton(
//                onClick = { deleteTecnico(tecnico) },
//                modifier = Modifier.size(48.dp)
//            ) {
//                Icon(
//                    Icons.Outlined.Delete,
//                    contentDescription = "Eliminar",
//                    tint = secondaryColor
//                )
//            }
        }
    }
}

