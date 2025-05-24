package edu.ucne.registrotecnico.presentation.prioridades

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoAp2Theme

@Composable
fun PrioridadListScreen(
    viewModel: PrioridadesViewModel = hiltViewModel(),
    goToPrioridad: (Int) -> Unit,
    createPrioridad: () -> Unit,
    deletePrioridad: ((PrioridadEntity) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PrioridadListBodyScreen(
        uiState = uiState,
        goToPrioridad = goToPrioridad,
        createPrioridad = createPrioridad,
        deletePrioridad = { prioridad ->
            viewModel.onEvent(PrioridadEvent.PrioridadChange(prioridad.prioridadId ?: 0))
            viewModel.onEvent(PrioridadEvent.Delete)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListBodyScreen(
    uiState: PrioridadUiState,
    goToPrioridad: (Int) -> Unit,
    createPrioridad: () -> Unit,
    deletePrioridad: (PrioridadEntity) -> Unit
) {
    // Definición de colores personalizados
    val primaryColor = Color(0xFF272D4D)
    val secondaryColor = Color(0xFFB83564)
    val accentColor = Color(0xFFFF6A5A)
    val lightAccentColor = Color(0xFFFFB350)
    val complementaryColor = Color(0xFF83B8AA)

    val priorityColors = mapOf(
        "Alta" to accentColor,
        "Media" to lightAccentColor,
        "Baja" to complementaryColor
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Prioridades",
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
                onClick = createPrioridad,
                containerColor = secondaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Agregar nueva prioridad")
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            // Card contenedor con mejor diseño
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
                if (uiState.prioridades.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay prioridades registradas",
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
                        items(uiState.prioridades) { prioridad ->
                            PrioridadCard(
                                prioridad = prioridad,
                                goToPrioridad = { goToPrioridad(prioridad.prioridadId ?: 0) },
                                deletePrioridad = deletePrioridad,
                                priorityColors = priorityColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrioridadCard(
    prioridad: PrioridadEntity,
    goToPrioridad: () -> Unit,
    deletePrioridad: (PrioridadEntity) -> Unit,
    priorityColors: Map<String, Color>
) {
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
            // Indicador visual de prioridad
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(
                        color = priorityColors[prioridad.descripcion] ?: Color.Gray,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = prioridad.descripcion,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = priorityColors[prioridad.descripcion] ?: Color.Black
                )
                Text(
                    text = "ID: ${prioridad.prioridadId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = goToPrioridad,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Editar",
                    tint = Color(0xFF272D4D)
                )
            }

            IconButton(
                onClick = { deletePrioridad(prioridad) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFB83564)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val prioridades = listOf(
        PrioridadEntity(
            prioridadId = 1,
            descripcion = "Alta"
        ),
        PrioridadEntity(
            prioridadId = 2,
            descripcion = "Media"
        ),
        PrioridadEntity(
            prioridadId = 3,
            descripcion = "Baja"
        ),
        PrioridadEntity(
            prioridadId = 4,
            descripcion = "Urgente"
        )
    )

    val mockUiState = PrioridadUiState(
        prioridades = prioridades
    )

    MaterialTheme {
        PrioridadListBodyScreen(
            uiState = mockUiState,
            goToPrioridad = {},
            createPrioridad = {},
            deletePrioridad = {}
        )
    }
}