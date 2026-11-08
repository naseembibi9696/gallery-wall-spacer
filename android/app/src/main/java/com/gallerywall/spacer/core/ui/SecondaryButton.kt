package com.gallerywall.spacer.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBg

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    light: Boolean = true,
    enabled: Boolean = true,
    height: Dp = 48.dp
) {
    val shape = RoundedCornerShape(10.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 1400f),
        label = "secondaryPress"
    )
    val contentColor = if (light) InkPrimary else PlasterBg
    val borderColor = if (light) InkPrimary else Color.White.copy(alpha = 0.35f)
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        enabled = enabled,
        shape = shape,
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContentColor = contentColor.copy(alpha = 0.4f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
        interactionSource = interactionSource
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
