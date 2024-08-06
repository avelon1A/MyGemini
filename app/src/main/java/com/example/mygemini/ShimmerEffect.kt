package com.example.mygemini

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.2f),
                        Color.Black.copy(alpha = 0.5f),
                        Color.Black.copy(alpha = 0.2f)
                    ),
                    start = Offset(translateAnim, 0f),
                    end = Offset(translateAnim + 1000f, 0f)
                )
            )
            .padding(8.dp)
            .width(100.dp)
        , contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loading...",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontSize = 16.sp
        )
    }
}