package com.gallerywall.spacer.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.core.theme.SurfaceCard

@Composable
fun NailRow(
    title: String,
    position: String,
    index: Int,
    done: Boolean,
    stateLabel: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(index) {
        appeared = true
    }
    val appearProgress by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 900f),
        label = "nailDrop"
    )
    val markerColor by animateColorAsState(
        targetValue = if (done) InkPrimary else CoralAccent,
        label = "nailMarker"
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .semantics { stateDescription = stateLabel },
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = if (done) 0.03f else 0.06f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer {
                        translationY = (appearProgress - 1f) * 12.dp.toPx()
                        scaleX = 0.6f + 0.4f * appearProgress
                        scaleY = 0.6f + 0.4f * appearProgress
                    }
                    .background(markerColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (done) {
                    Icon(
                        imageVector = AppIcons.Check,
                        contentDescription = null,
                        tint = CoralAccent,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = index.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SurfaceCard
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 13.sp,
                    color = PlasterBg.copy(alpha = if (done) 0.55f else 1f)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = position,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 12.sp,
                    color = PlasterBg.copy(alpha = if (done) 0.4f else 0.8f)
                )
            }
            Checkbox(
                checked = done,
                onCheckedChange = { onToggle() },
                modifier = Modifier.size(48.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = CoralAccent,
                    uncheckedColor = Color.White.copy(alpha = 0.45f),
                    checkmarkColor = SurfaceCard
                )
            )
        }
    }
}
