package com.gallerywall.spacer.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBgDeepest
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary
import com.gallerywall.spacer.core.theme.TextTertiary

data class MiniNail(
    val x: Float,
    val y: Float
)

@Composable
fun WallRow(
    name: String,
    summary: String,
    preview: List<MiniNail>,
    openLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .clickable(onClick = onClick)
            .semantics { stateDescription = openLabel },
        shape = RoundedCornerShape(10.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceMuted)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(
                modifier = Modifier
                    .size(36.dp, 28.dp)
            ) {
                drawRect(color = PlasterBgDeepest, size = size)
                drawLine(
                    color = InkPrimary,
                    start = Offset(0f, size.height - 1f),
                    end = Offset(size.width, size.height - 1f),
                    strokeWidth = 2f
                )
                preview.forEach { nail ->
                    drawCircle(
                        color = CoralAccent,
                        radius = 2.dp.toPx(),
                        center = Offset(nail.x * size.width, nail.y * size.height)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = InkDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = summary,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            ForwardGlyph(color = TextTertiary)
        }
    }
}
