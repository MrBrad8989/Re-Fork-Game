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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.parchismania.GameViewModel
import com.example.parchismania.data.BoardSkin

private data class ShopItem(val skin: BoardSkin, val title: String, val price: Int, val desc: String)

@Composable
fun ShopScreen(
    vm: GameViewModel,
    onBack: () -> Unit,
) {
    val wallet by vm.wallet.collectAsState()
    val settings by vm.settings.collectAsState()

    val items = listOf(
        ShopItem(BoardSkin.CLASSIC, "🎨 Classic", 0, "El diseño original del Parchís."),
        ShopItem(BoardSkin.MIDNIGHT, "🌙 Midnight", 120, "Tablero oscuro con alto contraste."),
        ShopItem(BoardSkin.CANDY, "🍬 Candy", 120, "Colores vibrantes y alegres."),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
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
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
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
                            "🛒 Tienda de Skins",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Personaliza tu tablero",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    OutlinedButton(
                        onClick = onBack,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Text("⬅️ Volver")
                    }
                }
            }

            // Balance
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "💰 Tu saldo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "${wallet.coins} 🪙",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Items de la tienda
            items.forEach { item ->
                val owned = item.skin in wallet.ownedSkins
                val selected = item.skin == settings.boardSkin
                val canBuy = !owned && wallet.coins >= item.price

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected)
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                    border = if (selected)
                        BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                    else
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (selected) 6.dp else 2.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                item.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (owned)
                                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                                    else
                                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    if (owned) "✓ Comprado" else "${item.price} 🪙",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            item.desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(4.dp))

                        when {
                            selected -> {
                                Button(
                                    onClick = {},
                                    enabled = false,
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("✓ Seleccionado", fontWeight = FontWeight.Bold)
                                }
                            }
                            owned -> {
                                ElevatedButton(
                                    onClick = { vm.selectSkin(item.skin) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("🎨 Seleccionar", fontWeight = FontWeight.Bold)
                                }
                            }
                            else -> {
                                ElevatedButton(
                                    onClick = {
                                        vm.buySkin(item.skin, item.price).also { ok ->
                                            if (ok) vm.selectSkin(item.skin)
                                        }
                                    },
                                    enabled = canBuy,
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        if (canBuy) "🛒 Comprar ahora" else "❌ Monedas insuficientes",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
