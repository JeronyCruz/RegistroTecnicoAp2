package edu.ucne.registrotecnico.presentation.tecnicos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.tooling.preview.Preview
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoAp2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListScreen(
    tecnicoList: List<TecnicoEntity>,
    onEditClick: (Int?) -> Unit,
    onDeleteClick: (TecnicoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de técnicos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEditClick(0) }) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(tecnicoList) { tecnico ->
                    TecnicoCard(
                        tecnico = tecnico,
                        onEditClick = { onEditClick(tecnico.tecnicoId) },
                        onDeleteClick = { onDeleteClick(tecnico) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
fun TecnicoCard(
    tecnico: TecnicoEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ID: ${tecnico.tecnicoId}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = tecnico.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = "$${"%.2f".format(tecnico.sueldo)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            IconButton(onClick = onEditClick) {
                Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Color.Green)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = Color.Green)
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
            nombre = "Juan",
            sueldo = 100.0
        ),
        TecnicoEntity(
            tecnicoId = 2,
            nombre = "Jose",
            sueldo = 200.0
        )
    )
    RegistroTecnicoAp2Theme {
        TecnicoListScreen(
            tecnicoList = tecnicos,
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}