package edu.ucne.registrotecnico.presentation.tecnicos

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
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity


@Composable
fun TecnicoListScreen(
    viewModel: TecnicosViewModel = hiltViewModel(),
    goToTecnico: (Int) -> Unit,
    createTecnico: () -> Unit,
    deleteTecnico: ((TecnicoEntity) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TecnicoListBodyScreen(
        uiState = uiState,
        goToTecnico = { id -> goToTecnico(id) },
        createTecnico = createTecnico,
        deleteTecnico = { tecnico ->
            viewModel.onEvent(TecnicoEvent.TecnicoChange(tecnico.tecnicoId ?: 0))
            viewModel.onEvent(TecnicoEvent.Delete)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListBodyScreen(
    uiState: TecnicoUiState,
    goToTecnico: (Int) -> Unit,
    createTecnico: () -> Unit,
    deleteTecnico: (TecnicoEntity) -> Unit
) {
    // Definición de colores personalizados
    val primaryColor = Color(0xFF272D4D)
    val secondaryColor = Color(0xFFB83564)
    val accentColor = Color(0xFFFF6A5A)
    val lightAccentColor = Color(0xFFFFB350)
    val complementaryColor = Color(0xFF83B8AA)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Técnicos",
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
                onClick = createTecnico,
                containerColor = secondaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Agregar nuevo técnico")
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
                if (uiState.tecnico.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay técnicos registrados",
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
                        items(uiState.tecnico) { tecnico ->
                            TecnicoCard(
                                tecnico = tecnico,
                                goToTecnico = { goToTecnico(tecnico.tecnicoId ?: 0) },
                                deleteTecnico = deleteTecnico,
                                primaryColor = primaryColor,
                                secondaryColor = secondaryColor
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
fun TecnicoCard(
    tecnico: TecnicoEntity,
    goToTecnico: () -> Unit,
    deleteTecnico: (TecnicoEntity) -> Unit,
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
                    text = tecnico.nombre.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tecnico.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "ID: ${tecnico.tecnicoId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = "$${"%.2f".format(tecnico.sueldo)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = complementaryColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = goToTecnico,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Editar",
                    tint = primaryColor
                )
            }

            IconButton(
                onClick = { deleteTecnico(tecnico) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    tint = secondaryColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val tecnicos = listOf(
        TecnicoEntity(
            tecnicoId = 1,
            nombre = "Juan Pérez",
            sueldo = 1250.50
        ),
        TecnicoEntity(
            tecnicoId = 2,
            nombre = "María García",
            sueldo = 1450.75
        ),
        TecnicoEntity(
            tecnicoId = 3,
            nombre = "Carlos López",
            sueldo = 1100.00
        )
    )

    val mockUiState = TecnicoUiState(
        tecnico = tecnicos
    )

    MaterialTheme {
        TecnicoListBodyScreen(
            uiState = mockUiState,
            goToTecnico = {},
            createTecnico = {},
            deleteTecnico = {}
        )
    }
}