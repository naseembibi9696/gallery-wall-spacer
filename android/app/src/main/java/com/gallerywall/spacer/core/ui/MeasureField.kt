package com.gallerywall.spacer.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary

@Composable
fun MeasureField(
    label: String,
    value: String,
    focused: Boolean,
    stateLabel: String,
    onFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (focused) InkPrimary else SurfaceMuted,
        label = "measureBorder"
    )
    Surface(
        modifier = modifier
            .height(72.dp)
            .clickable(onClick = onFocus)
            .semantics { stateDescription = stateLabel },
        shape = RoundedCornerShape(10.dp),
        color = SurfaceCard,
        border = BorderStroke(if (focused) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                letterSpacing = 1.0.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = InkDeep
            )
        }
    }
}
