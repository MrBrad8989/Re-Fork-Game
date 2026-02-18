package com.example.parchismania.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.parchismania.engine.GameEngine
import com.example.parchismania.engine.GameState
import com.example.parchismania.engine.Piece
import com.example.parchismania.engine.PlayerColor
import com.example.parchismania.data.BoardSkin
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.getValue
import kotlin.math.roundToInt

@Composable
fun ParchisBoard(
    gameState: GameState,
    movablePieces: List<String>,
    onPieceClick: (String) -> Unit,
    animationsEnabled: Boolean,
    skin: BoardSkin,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.shadow(12.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp))) {
        val boardPx = constraints.maxWidth.toFloat()
        val cell = boardPx / 15f
        val pieceSizePx = cell * 0.70f
        val density = LocalDensity.current

        // ✅ Saca colores fuera del Canvas (MaterialTheme es @Composable)
        val primary = MaterialTheme.colorScheme.primary

        val bg = when (skin) {
            BoardSkin.CLASSIC -> Color(0xFFF5F5DC) // Beige claro
            BoardSkin.MIDNIGHT -> Color(0xFF1A1A2E) // Azul oscuro
            BoardSkin.CANDY -> Color(0xFFFFF0F5) // Rosa muy suave
        }

        val borderColor = when (skin) {
            BoardSkin.CLASSIC -> Color(0xFFD4AF37) // Dorado
            BoardSkin.MIDNIGHT -> Color(0xFF6C63FF) // Púrpura brillante
            BoardSkin.CANDY -> Color(0xFFFF69B4) // Rosa intenso
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Fondo del tablero con esquinas redondeadas
                drawRoundRect(
                    color = bg,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
                )

                // Borde decorativo exterior
                drawRoundRect(
                    color = borderColor.copy(alpha = 0.6f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f),
                    style = Stroke(width = 4f)
                )

                // Home bases (zonas de salida con gradiente)
                fun tint(c: PlayerColor): Color {
                    val baseColor = colorFor(c)
                    return when (skin) {
                        BoardSkin.CLASSIC -> baseColor.copy(alpha = 0.20f)
                        BoardSkin.MIDNIGHT -> baseColor.copy(alpha = 0.35f)
                        BoardSkin.CANDY -> baseColor.copy(alpha = 0.25f)
                    }
                }

                drawRoundRect(
                    tint(PlayerColor.YELLOW),
                    topLeft = androidx.compose.ui.geometry.Offset(cell * 0.3f, cell * 0.3f),
                    size = androidx.compose.ui.geometry.Size(cell * 5.4f, cell * 5.4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cell * 0.4f, cell * 0.4f)
                )
                drawRoundRect(
                    tint(PlayerColor.BLUE),
                    topLeft = androidx.compose.ui.geometry.Offset(cell * 9.3f, cell * 0.3f),
                    size = androidx.compose.ui.geometry.Size(cell * 5.4f, cell * 5.4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cell * 0.4f, cell * 0.4f)
                )
                drawRoundRect(
                    tint(PlayerColor.RED),
                    topLeft = androidx.compose.ui.geometry.Offset(cell * 9.3f, cell * 9.3f),
                    size = androidx.compose.ui.geometry.Size(cell * 5.4f, cell * 5.4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cell * 0.4f, cell * 0.4f)
                )
                drawRoundRect(
                    tint(PlayerColor.GREEN),
                    topLeft = androidx.compose.ui.geometry.Offset(cell * 0.3f, cell * 9.3f),
                    size = androidx.compose.ui.geometry.Size(cell * 5.4f, cell * 5.4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cell * 0.4f, cell * 0.4f)
                )

                // Main path cells
                for (i in 1..68) {
                    val pos = BoardPositions.mainPath[i] ?: continue
                    val x = pos.x * cell
                    val y = pos.y * cell

                    val isSafe = GameEngine.SAFE_POSITIONS.contains(i)
                    val startColor = GameEngine.START_POSITIONS.entries.firstOrNull { it.value == i }?.key
                    val fill = when {
                        startColor != null -> colorFor(startColor).copy(alpha = 0.22f)
                        isSafe -> Color(0xFFFFD54F).copy(alpha = 0.18f)
                        else -> Color.White.copy(alpha = 0.06f)
                    }
                    val stroke = when {
                        startColor != null -> colorFor(startColor).copy(alpha = 0.35f)
                        isSafe -> Color(0xFFFFD54F).copy(alpha = 0.30f)
                        else -> Color.White.copy(alpha = 0.10f)
                    }

                    drawRoundRect(
                        color = fill,
                        topLeft = androidx.compose.ui.geometry.Offset(x + 1f, y + 1f),
                        size = androidx.compose.ui.geometry.Size(cell - 2f, cell - 2f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                        style = Fill
                    )
                    drawRoundRect(
                        color = stroke,
                        topLeft = androidx.compose.ui.geometry.Offset(x + 1f, y + 1f),
                        size = androidx.compose.ui.geometry.Size(cell - 2f, cell - 2f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                        style = Stroke(width = 1f)
                    )
                }

                // Home stretch cells
                for ((color, positions) in BoardPositions.homeStretch) {
                    val fill = colorFor(color).copy(alpha = 0.18f)
                    val stroke = colorFor(color).copy(alpha = 0.35f)
                    positions.dropLast(1).forEach { p ->
                        val x = p.x * cell
                        val y = p.y * cell
                        drawRoundRect(
                            fill,
                            topLeft = androidx.compose.ui.geometry.Offset(x + 1f, y + 1f),
                            size = androidx.compose.ui.geometry.Size(cell - 2f, cell - 2f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                        )
                        drawRoundRect(
                            stroke,
                            topLeft = androidx.compose.ui.geometry.Offset(x + 1f, y + 1f),
                            size = androidx.compose.ui.geometry.Size(cell - 2f, cell - 2f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                            style = Stroke(width = 1f)
                        )
                    }
                }

                // ✅ Center (FIX: usa primary, NO MaterialTheme dentro del Canvas)
                val cx = 7f * cell + cell / 2f
                val cy = 7f * cell + cell / 2f
                drawCircle(
                    color = primary.copy(alpha = 0.16f),
                    radius = cell * 0.9f,
                    center = androidx.compose.ui.geometry.Offset(cx, cy)
                )
                drawCircle(
                    color = primary.copy(alpha = 0.28f),
                    radius = cell * 0.7f,
                    center = androidx.compose.ui.geometry.Offset(cx, cy),
                    style = Stroke(width = 2f)
                )
            }

            // Piece stacking offsets (similar idea to TS)
            val groups = mutableMapOf<String, MutableList<String>>()
            for (pl in gameState.players) {
                for (pc in pl.pieces) {
                    if (pc.isFinished) continue
                    val key = when {
                        pc.isHome -> "home-${pc.color}-${pc.id.substringAfter('-')}"
                        pc.homeIndex > 0 -> "hs-${pc.color}-${pc.homeIndex}"
                        else -> "pos-${pc.position}"
                    }
                    groups.getOrPut(key) { mutableListOf() }.add(pc.id)
                }
            }

            for (pl in gameState.players) {
                for (pc in pl.pieces) {
                    if (pc.isFinished) continue
                    val key = when {
                        pc.isHome -> "home-${pc.color}-${pc.id.substringAfter('-')}"
                        pc.homeIndex > 0 -> "hs-${pc.color}-${pc.homeIndex}"
                        else -> "pos-${pc.position}"
                    }
                    val group = groups[key] ?: mutableListOf(pc.id)
                    val idx = group.indexOf(pc.id).coerceAtLeast(0)

                    val pos = pieceCenter(pc, groupCount = group.size, index = idx, cell = cell)
                    val isMovable = movablePieces.contains(pc.id)

                    val offsetX = (pos.first - pieceSizePx / 2f).roundToInt()
                    val offsetY = (pos.second - pieceSizePx / 2f).roundToInt()

                    val target = IntOffset(offsetX, offsetY)

                    val animatedOffset by animateIntOffsetAsState(
                        targetValue = target,
                        animationSpec = tween(durationMillis = if (animationsEnabled) 260 else 0),
                        label = "pieceOffset-${pc.id}"
                    )

                    Box(
                        modifier = Modifier
                            .offset { animatedOffset }
                            .size(with(density) { pieceSizePx.toDp() })
                            .shadow(
                                elevation = if (isMovable) 8.dp else 4.dp,
                                shape = CircleShape,
                                clip = false
                            )
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        colorFor(pc.color).copy(alpha = if (isMovable) 1.0f else 0.85f),
                                        colorFor(pc.color).copy(alpha = if (isMovable) 0.85f else 0.70f)
                                    )
                                )
                            )
                            .then(if (isMovable) Modifier.clickable { onPieceClick(pc.id) } else Modifier),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(Modifier.fillMaxSize()) {
                            // Brillo interno
                            drawCircle(
                                Color.White.copy(alpha = if (isMovable) 0.40f else 0.25f),
                                radius = size.minDimension * 0.28f
                            )
                            // Borde interno
                            drawCircle(
                                Color.Black.copy(alpha = 0.10f),
                                radius = size.minDimension * 0.48f,
                                style = Stroke(width = 1.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun colorFor(c: PlayerColor): Color = when (c) {
    PlayerColor.YELLOW -> Color(0xFFFFEB3B)
    PlayerColor.BLUE -> Color(0xFF2196F3)
    PlayerColor.RED -> Color(0xFFF44336)
    PlayerColor.GREEN -> Color(0xFF4CAF50)
}

private fun pieceCenter(piece: Piece, groupCount: Int, index: Int, cell: Float): Pair<Float, Float> {
    return when {
        piece.isHome -> {
            val homeIdx = piece.id.substringAfter('-').toIntOrNull() ?: 0
            val base = BoardPositions.homeBases.getValue(piece.color).getOrNull(homeIdx)
                ?: BoardPositions.homeBases.getValue(piece.color).first()
            val cx = base.x * cell + cell * 0.35f
            val cy = base.y * cell + cell * 0.35f
            cx to cy
        }
        piece.homeIndex > 0 -> {
            val p = BoardPositions.homeStretch.getValue(piece.color)[piece.homeIndex - 1]
            val cx = p.x * cell + cell / 2f
            val cy = p.y * cell + cell / 2f
            cx to cy
        }
        else -> {
            val p = BoardPositions.mainPath[piece.position] ?: BoardPos(7f, 7f)
            val baseX = p.x * cell + cell / 2f
            val baseY = p.y * cell + cell / 2f
            val offsetX = if (groupCount > 1) (index - (groupCount - 1) / 2f) * 5f else 0f
            val offsetY = if (groupCount > 1) if (index % 2 == 0) -3f else 3f else 0f
            (baseX + offsetX) to (baseY + offsetY)
        }
    }
}