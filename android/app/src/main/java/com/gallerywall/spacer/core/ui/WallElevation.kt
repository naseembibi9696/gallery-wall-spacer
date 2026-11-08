package com.gallerywall.spacer.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallerywall.spacer.R
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.SnapAxis
import com.gallerywall.spacer.domain.model.SnapGuide
import kotlin.math.max
import kotlin.math.min

data class FrameLabel(
    val description: String,
    val state: String
)

@Composable
fun WallElevation(
    wallWidthCm: Float,
    wallHeightCm: Float,
    furnitureTopCm: Float,
    eyeHeightCm: Float,
    eyeLabel: String,
    floorLabel: String,
    frames: List<FrameSpec>,
    placements: List<PlacedFrame>,
    guides: List<SnapGuide>,
    selectedFrameId: Int,
    invalidFrameId: Int,
    interactive: Boolean,
    frameLabels: Map<Int, FrameLabel>,
    onDragStart: (Int) -> Unit,
    onDrag: (Int, Float, Float) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val scale = min(maxWidth.value / wallWidthCm, maxHeight.value / wallHeightCm)
        val density = LocalDensity.current.density
        val shape = RoundedCornerShape(10.dp)
        val byId = frames.associateBy { it.id }
        Box(
            modifier = Modifier
                .size((wallWidthCm * scale).dp, (wallHeightCm * scale).dp)
                .clip(shape)
                .border(1.dp, SurfaceMuted, shape)
        ) {
            Image(
                painter = painterResource(R.drawable.bg_game),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PlasterBg.copy(alpha = 0.78f))
            )

            if (furnitureTopCm > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height((furnitureTopCm * scale).dp)
                        .background(SurfaceMuted.copy(alpha = 0.55f))
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val eyeY = (wallHeightCm - eyeHeightCm) * scale * density
                drawLine(
                    color = CoralAccent.copy(alpha = 0.45f),
                    start = Offset(0f, eyeY),
                    end = Offset(size.width, eyeY),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                )
                guides.forEach { guide ->
                    if (guide.axis == SnapAxis.VERTICAL) {
                        val gx = guide.positionCm * scale * density
                        drawLine(
                            color = CoralAccent,
                            start = Offset(gx, 0f),
                            end = Offset(gx, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    } else {
                        val gy = (wallHeightCm - guide.positionCm) * scale * density
                        drawLine(
                            color = CoralAccent,
                            start = Offset(0f, gy),
                            end = Offset(size.width, gy),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }

            Text(
                text = eyeLabel,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = CoralAccent,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = ((wallHeightCm - eyeHeightCm) * scale).dp + 3.dp)
                    .padding(end = 6.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(InkPrimary)
            )
            Text(
                text = floorLabel,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = InkPrimary,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 6.dp, bottom = 6.dp)
            )

            placements.forEach { placed ->
                val spec = byId[placed.frameId]
                if (spec != null) {
                    key(spec.id) {
                        val frameW = (spec.widthCm * scale).dp
                        val frameH = (spec.heightCm * scale).dp
                        val touchW = max(frameW.value, TOUCH_MIN).dp
                        val touchH = max(frameH.value, TOUCH_MIN).dp
                        val targetX = (placed.xCm * scale).dp - (touchW - frameW) / 2
                        val targetY =
                            ((wallHeightCm - placed.yCm - spec.heightCm) * scale).dp -
                                (touchH - frameH) / 2
                        val animatedX by animateDpAsState(
                            targetValue = targetX,
                            animationSpec = spring(dampingRatio = 0.85f, stiffness = 1600f),
                            label = "frameX"
                        )
                        val animatedY by animateDpAsState(
                            targetValue = targetY,
                            animationSpec = spring(dampingRatio = 0.85f, stiffness = 1600f),
                            label = "frameY"
                        )
                        val label = frameLabels[spec.id]
                        val selected = spec.id == selectedFrameId
                        val invalid = spec.id == invalidFrameId
                        Box(
                            modifier = Modifier
                                .offset(x = animatedX, y = animatedY)
                                .size(touchW, touchH)
                                .semantics {
                                    if (label != null) {
                                        contentDescription = label.description
                                        stateDescription = label.state
                                    }
                                }
                                .pointerInput(spec.id, interactive, scale) {
                                    if (interactive) {
                                        detectDragGestures(
                                            onDragStart = { onDragStart(spec.id) },
                                            onDragEnd = { onDragEnd() },
                                            onDragCancel = { onDragEnd() },
                                            onDrag = { change, amount ->
                                                change.consume()
                                                onDrag(
                                                    spec.id,
                                                    amount.x / (scale * density),
                                                    -amount.y / (scale * density)
                                                )
                                            }
                                        )
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                modifier = Modifier.size(frameW, frameH),
                                shape = RoundedCornerShape(4.dp),
                                color = SurfaceCard,
                                border = BorderStroke(
                                    if (selected || invalid) 2.dp else 1.dp,
                                    when {
                                        invalid -> CoralAccent
                                        selected -> InkPrimary
                                        else -> InkPrimary.copy(alpha = 0.7f)
                                    }
                                ),
                                shadowElevation = if (selected) 10.dp else 4.dp
                            ) {
                                Image(
                                    painter = painterResource(spriteFor(spec.spriteIndex)),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(3.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            if (selected) {
                                Text(
                                    text = spec.id.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SurfaceCard,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = (touchW - frameW) / 2, y = (touchH - frameH) / 2)
                                        .size(16.dp)
                                        .background(CoralAccent, CircleShape)
                                        .padding(top = 1.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val TOUCH_MIN = 56f
