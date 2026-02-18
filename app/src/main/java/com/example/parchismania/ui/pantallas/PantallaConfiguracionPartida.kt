package com.example.parchismania.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parchismania.GameViewModel
import com.example.parchismania.PlayerSetup
import com.example.parchismania.engine.PlayerColor

@Composable
fun PantallaConfiguracionPartida(
    vm: GameViewModel,
    onIniciar: () -> Unit,
    onVolver: () -> Unit,
) {
    val setup by vm.setup.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Nueva Partida",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Configura tu partida",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    OutlinedButton(
                        onClick = onVolver,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Volver")
                    }
                }
            }

            // Configuración
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Número de jugadores
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Número de jugadores",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            (2..4).forEach { n ->
                                SelectorChip(
                                    label = n.toString(),
                                    selected = setup.playersCount == n,
                                    onClick = {
                                        val next = normalize(
                                            setup.copy(
                                                playersCount = n,
                                                humanCount = setup.humanCount.coerceAtMost(n)
                                            )
                                        )
                                        vm.updateSetup(next)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Jugadores humanos
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Jugadores humanos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            (1..setup.playersCount).forEach { n ->
                                SelectorChip(
                                    label = n.toString(),
                                    selected = setup.humanCount == n,
                                    onClick = {
                                        vm.updateSetup(normalize(setup.copy(humanCount = n)))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Colores de jugadores
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Colores de los jugadores",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        (0 until setup.playersCount).forEach { idx ->
                            val current = setup.colors.getOrNull(idx) ?: PlayerColor.YELLOW
                            val isHuman = idx < setup.humanCount

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.5f
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        if (isHuman) "Jugador ${idx + 1}" else "CPU ${idx + 1 - setup.humanCount}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        PlayerColor.entries.forEach { color ->
                                            SelectorColorCirculo(
                                                color = color,
                                                selected = current == color,
                                                enabled = !setup.colors.take(setup.playersCount)
                                                    .filterIndexed { i, _ -> i != idx }
                                                    .contains(color),
                                                onClick = {
                                                    val newColors =
                                                        setup.colors.toMutableList().apply {
                                                            while (size < 4) add(
                                                                PlayerColor.entries[size]
                                                            )
                                                            this[idx] = color
                                                        }
                                                    vm.updateSetup(
                                                        normalize(
                                                            setup.copy(
                                                                colors = newColors
                                                            )
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Botón de inicio
            ElevatedButton(
                onClick = {
                    vm.startFromSetup()
                    onIniciar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Iniciar Partida",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

private fun normalize(s: PlayerSetup): PlayerSetup {
    val players = s.playersCount.coerceIn(2, 4)
    val humans = s.humanCount.coerceIn(1, players)
    val colors = (s.colors + PlayerColor.entries).distinct().take(4)
    return s.copy(playersCount = players, humanCount = humans, colors = colors)
}

@Composable
private fun SelectorChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            2.dp,
            if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 1.dp
        )
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun SelectorColorCirculo(
    color: PlayerColor,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val colorReal = when (color) {
        PlayerColor.YELLOW -> Color(0xFFFFC107)
        PlayerColor.BLUE -> Color(0xFF2196F3)
        PlayerColor.RED -> Color(0xFFF44336)
        PlayerColor.GREEN -> Color(0xFF4CAF50)
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Círculo exterior (indicador de selección)
        if (selected) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        colorReal.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .then(
                        if (enabled) Modifier else Modifier
                    )
            )
        }

        // Círculo del color
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    if (enabled) colorReal else colorReal.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        ) {
            if (selected) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "✓",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (!enabled && !selected) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "✕",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

