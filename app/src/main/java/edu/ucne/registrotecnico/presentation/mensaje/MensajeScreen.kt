package edu.ucne.registrotecnico.presentation.mensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MensajeScreen(
    ticketId: Int,
    onBackClick: () -> Unit,
    viewModel: MensajesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(ticketId) {
        viewModel.onEvent(MensajeEvent.TicketIdChange(ticketId))
    }

    MensajeBodyScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeBodyScreen(
    uiState: MensajeUiState,
    onBackClick: () -> Unit,
    onEvent: (MensajeEvent) -> Unit
) {
    val primaryColor = Color(0xFF272D4D)
    val secondaryColor = Color(0xFFB83564)
    val operatorColor = Color(0xFF4A89DC)
    val ownerColor = Color(0xFF48CFAD)
    val backgroundColor = Color(0xFFF5F7FA)

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.mensajes.size) {
        if (uiState.mensajes.isNotEmpty()) {
            listState.animateScrollToItem(uiState.mensajes.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ticket #${uiState.ticketId}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ordenamos mensajes cronológicamente (más antiguos arriba)
                items(uiState.mensajes.sortedByDescending { it.fecha }) { mensaje ->
                    MensajeItem(
                        mensaje = mensaje,
                        isOwner = mensaje.tipoRemitente == "Owner",
                        ownerColor = ownerColor,
                        operatorColor = operatorColor
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Radio buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RadioButton(
                            selected = uiState.tipoRemitente == "Owner",
                            onClick = { onEvent(MensajeEvent.tipoRemitenteChange("Owner")) },
                            colors = RadioButtonDefaults.colors(selectedColor = ownerColor)
                        )
                        Text("Owner")

                        RadioButton(
                            selected = uiState.tipoRemitente == "Operator",
                            onClick = { onEvent(MensajeEvent.tipoRemitenteChange("Operator")) },
                            colors = RadioButtonDefaults.colors(selectedColor = operatorColor)
                        )
                        Text("Operator")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre
                    OutlinedTextField(
                        value = uiState.remitente ?: "",
                        onValueChange = { onEvent(MensajeEvent.RemitenteChange(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        label = { Text("Your name") },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = primaryColor
                        ),
                        singleLine = true
                    )

                    // Campo mensaje + botón
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = uiState.contenido ?: "",
                            onValueChange = { onEvent(MensajeEvent.ContenidoChange(it)) },
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White),
                            shape = RoundedCornerShape(24.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color.LightGray,
                                cursorColor = primaryColor
                            ),
                            placeholder = { Text("Escribe tu mensaje...") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (uiState.remitente?.isNotBlank() == true) {
                                        onEvent(MensajeEvent.Save)
                                        focusManager.clearFocus()
                                    }
                                }
                            ),
                            singleLine = false,
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (uiState.remitente?.isNotBlank() == true) {
                                    onEvent(MensajeEvent.Save)
                                    focusManager.clearFocus()
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(secondaryColor, CircleShape),
                            enabled = uiState.remitente?.isNotBlank() == true && uiState.contenido?.isNotBlank() == true
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Enviar",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MensajeItem(
    mensaje: MensajeEntity,
    isOwner: Boolean,
    ownerColor: Color,
    operatorColor: Color
) {
    // Aquí definimos si el mensaje va a la izquierda o derecha según sea Owner u Operator
    val alignment = if (isOwner) Alignment.Start else Alignment.End
    val bubbleColor = if (isOwner) ownerColor else operatorColor
    val bubbleBackground = if (isOwner) bubbleColor.copy(alpha = 0.15f) else bubbleColor
    val textColor = if (isOwner) Color.Black else Color.White
    val borderColor = if (isOwner) bubbleColor.copy(alpha = 0.4f) else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Text(
            text = "Por ${mensaje.remitente} (${mensaje.tipoRemitente}) - ${mensaje.fecha.toFormattedDate()}",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bubbleBackground)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = mensaje.contenido ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = textColor,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.1
                )
            )
        }
    }
}

fun Date.toFormattedDate(): String {
    val format = SimpleDateFormat("dd/MM/yyyy (HH:mm)", Locale.getDefault())
    return format.format(this)
}


@Preview(showBackground = true)
@Composable
private fun PreviewMensajeScreen() {
    val mensajes = listOf(
        MensajeEntity(
            mensajeId = 1,
            contenido = "Gracias por su Tiempo, le estaremos atendiendo pronto.",
            fecha = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("01/06/2025 20:10")!!,
            remitente = "Operator",
            ticketId = 1
        ),
        MensajeEntity(
            mensajeId = 2,
            contenido = "Muchas Gracias.",
            fecha = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("01/06/2025 18:15")!!,
            remitente = "John Doe",
            ticketId = 1
        )
    )

    val mockUiState = MensajeUiState(
        mensajes = mensajes,
        ticketId = 77620,
        contenido = "",
        remitente = "User Name"
    )

    MaterialTheme {
        MensajeBodyScreen(
            uiState = mockUiState,
            onBackClick = {},
            onEvent = {}
        )
    }
}