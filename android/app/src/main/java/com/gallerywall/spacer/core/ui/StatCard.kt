package com.gallerywall.spacer.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary

@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    height: Dp = 78.dp
) {
    val container = if (dark) Color.White.copy(alpha = 0.07f) else SurfaceCard
    val border = if (dark) Color.White.copy(alpha = 0.14f) else SurfaceMuted
    val labelColor = if (dark) PlasterBg.copy(alpha = 0.66f) else TextSecondary
    val valueColor = if (dark) PlasterBg else InkDeep
    Surface(
        modifier = modifier.height(height),
        shape = RoundedCornerShape(10.dp),
        color = container,
        border = BorderStroke(1.dp, border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Spacer(
                Modifier
                    .size(8.dp)
                    .background(CoralAccent, CircleShape)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = labelColor
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = valueColor
            )
        }
    }
}
