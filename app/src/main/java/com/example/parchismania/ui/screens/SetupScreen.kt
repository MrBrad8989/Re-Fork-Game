package com.example.parchismania.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun SetupScreen(
    vm: GameViewModel,
    onStart: () -> Unit,
    onOpenShop: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val setup by vm.setup.collectAsState()
    val wallet by vm.wallet.collectAsState()

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Re-Fork Parchis",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Kotlin - Android Studio",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ElevatedButton(
                            onClick = onOpenShop,
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
                        ) {
                            Text("Tienda (" + wallet.coins.toString() + ")", fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = onOpenSettings,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Ajustes")
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Configurar partida",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Numero de jugadores",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (2..4).forEach { n ->
                                Chip(
                                    label = n.toString(),
                                    selected = setup.playersCount == n,
                                    onClick = {
                                        val next = normalize(setup.copy(playersCount = n, humanCount = setup.humanCount.coerceAtMost(n)))
                                        vm.updateSetup(next)
                                    }
                                )
                            }
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Jugadores humanos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (1..setup.playersCount).forEach { n ->
                                Chip(
                                    label = n.toString(),
                                    selected = setup.humanCount == n,
                                    onClick = { vm.updateSetup(normalize(setup.copy(humanCount = n))) }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Colores de jugadores",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        (0 until setup.playersCount).forEach { idx ->
                            val current = setup.colors.getOrNull(idx) ?: PlayerColor.YELLOW
                            val isHuman = idx < setup.humanCount
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        if (isHuman) "Jugador " + (idx + 1).toString() else "CPU " + (idx + 1 - setup.humanCount).toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ColorChip(current)
                                        Button(
                                            onClick = {
                                                val nextColor = nextUniqueColor(setup.colors, idx)
                                                val next = setup.colors.toMutableList().apply {
                                                    while (size < 4) add(PlayerColor.entries[size])
                                                    this[idx] = nextColor
                                                }
                                                vm.updateSetup(normalize(setup.copy(colors = next)))
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text("Cambiar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ElevatedButton(
                onClick = {
                    vm.startFromSetup()
                    onStart()
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
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
                    "Empezar partida",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Se guarda automaticamente: configuracion, ajustes, tienda y skins (DataStore).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
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

private fun nextUniqueColor(colors: List<PlayerColor>, idx: Int): PlayerColor {
    val used = colors.toMutableList()
    val current = colors.getOrNull(idx) ?: PlayerColor.YELLOW
    val available = PlayerColor.entries.filter { c -> c == current || c !in used.filterIndexed { i, _ -> i != idx } }
    val currentIndex = available.indexOf(current).coerceAtLeast(0)
    return available[(currentIndex + 1) % available.size]
}

@Composable
private fun Chip(label: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(2.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 4.dp else 1.dp)
    ) {
        Button(
            onClick = onClick, modifier = Modifier.height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, fontSize = 16.sp) }
    }
}

@Composable
private fun ColorChip(color: PlayerColor) {
    val name = when (color) {
        PlayerColor.YELLOW -> "Amarillo"
        PlayerColor.BLUE -> "Azul"
        PlayerColor.RED -> "Rojo"
        PlayerColor.GREEN -> "Verde"
    }
    val bg = when (color) {
        PlayerColor.YELLOW -> Color(0xFFFFEB3B)
        PlayerColor.BLUE -> Color(0xFF2196F3)
        PlayerColor.RED -> Color(0xFFF44336)
        PlayerColor.GREEN -> Color(0xFF4CAF50)
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = bg.copy(alpha = 0.9f)),
        modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(name, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

