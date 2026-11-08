package com.gallerywall.spacer.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary
import com.gallerywall.spacer.core.theme.TextTertiary

@Composable
fun StepperRow(
    label: String,
    value: String,
    decrementLabel: String,
    incrementLabel: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
    decrementEnabled: Boolean = true,
    incrementEnabled: Boolean = true
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(10.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceMuted)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(14.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = InkDeep,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Spacer(Modifier.width(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onDecrement,
                    enabled = decrementEnabled,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = decrementLabel }
                ) {
                    MinusGlyph(color = if (decrementEnabled) InkPrimary else TextTertiary)
                }
                IconButton(
                    onClick = onIncrement,
                    enabled = incrementEnabled,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = incrementLabel }
                ) {
                    PlusGlyph(color = if (incrementEnabled) InkPrimary else TextTertiary)
                }
            }
            Spacer(Modifier.width(4.dp))
        }
    }
}
