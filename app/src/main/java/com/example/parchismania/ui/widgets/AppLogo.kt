package com.example.parchismania.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Logo personalizado para Re-Fork Game
 * Representa un dado estilizado con las iniciales RF
 */
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            )
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Letras RF en grande
            Row {
                Text(
                    text = "R",
                    fontSize = (size * 0.35).sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "F",
                    fontSize = (size * 0.35).sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Puntos de dado decorativos
            Row {
                DiceDot()
                Spacer(Modifier.width(8.dp))
                DiceDot()
                Spacer(Modifier.width(8.dp))
                DiceDot()
                Spacer(Modifier.width(8.dp))
                DiceDot()
            }
        }
    }
}

@Composable
private fun DiceDot() {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.8f))
    )
}

/**
 * Logo alternativo más simple (solo dado estilizado)
 */
@Composable
fun SimpleDiceLogo(
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.primary
                    )
                )
            )
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Puntos del dado en patrón 5
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                DiceDotLarge()
                Spacer(Modifier.width(28.dp))
                DiceDotLarge()
            }
            Spacer(Modifier.height(16.dp))
            DiceDotLarge()
            Spacer(Modifier.height(16.dp))
            Row {
                DiceDotLarge()
                Spacer(Modifier.width(28.dp))
                DiceDotLarge()
            }
        }
    }
}

@Composable
private fun DiceDotLarge() {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            )
    )
}

