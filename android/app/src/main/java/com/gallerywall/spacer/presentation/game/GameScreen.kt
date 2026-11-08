package com.gallerywall.spacer.presentation.game

import com.gallerywall.spacer.core.ui.AppIcons
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallerywall.spacer.R
import com.gallerywall.spacer.core.di.AppViewModelFactory
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.core.theme.PlasterBgDeep
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary
import com.gallerywall.spacer.core.ui.BackGlyph
import com.gallerywall.spacer.core.ui.FrameLabel
import com.gallerywall.spacer.core.ui.PrimaryButton
import com.gallerywall.spacer.core.ui.SecondaryButton
import com.gallerywall.spacer.core.ui.WallElevation
import com.gallerywall.spacer.domain.model.ArrangementIssue

@Composable
fun GameScreen(
    onBack: () -> Unit,
    onOpenResult: (String) -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptics = LocalHapticFeedback.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                GameEvent.Snapped -> haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                is GameEvent.OpenResult -> onOpenResult(event.planId)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.commitDrag() }
    }
    GameContent(
        state = state,
        onBack = onBack,
        onOpenSettings = onOpenSettings,
        onDragStart = viewModel::startDrag,
        onDrag = viewModel::drag,
        onDragEnd = viewModel::endDrag,
        onRecentre = viewModel::recentre,
        onCalculate = viewModel::calculateNails
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onDragStart: (Int) -> Unit,
    onDrag: (Int, Float, Float) -> Unit,
    onDragEnd: () -> Unit,
    onRecentre: () -> Unit,
    onCalculate: () -> Unit
) {
    val frameLabels = mutableMapOf<Int, FrameLabel>()
    state.frames.forEach { frame ->
        val placed = state.placements.firstOrNull { it.frameId == frame.id }
        frameLabels[frame.id] = FrameLabel(
            description = stringResource(
                R.string.cd_frame,
                frame.id,
                frame.widthCm.toInt(),
                frame.heightCm.toInt()
            ),
            state = stringResource(
                R.string.state_frame_position,
                placed?.xCm?.toInt() ?: 0,
                placed?.yCm?.toInt() ?: 0
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PlasterBg, PlasterBgDeep)))
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.10f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = PlasterBg.copy(alpha = 0.92f)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                            BackGlyph(color = InkPrimary)
                        }
                        Spacer(Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.game_title),
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 13.sp,
                                letterSpacing = 2.2.sp,
                                color = InkPrimary
                            )
                            Text(
                                text = stringResource(
                                    R.string.wall_dimensions,
                                    state.wallWidthLabel,
                                    state.wallHeightLabel
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                letterSpacing = 0.8.sp,
                                color = InkPrimary.copy(alpha = 0.55f)
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = onOpenSettings, modifier = Modifier.size(48.dp)) {
                            Image(
                                painter = painterResource(R.drawable.icon_pause),
                                contentDescription = stringResource(R.string.cd_hold),
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = SurfaceMuted)
                }
            }

            WallElevation(
                wallWidthCm = state.wallWidthCm,
                wallHeightCm = state.wallHeightCm,
                furnitureTopCm = state.furnitureTopCm,
                eyeHeightCm = state.eyeHeightCm,
                eyeLabel = stringResource(R.string.eye_label, state.eyeHeightLabel),
                floorLabel = stringResource(R.string.floor),
                frames = state.frames,
                placements = state.placements,
                guides = state.guides,
                selectedFrameId = state.selectedFrameId,
                invalidFrameId = state.invalidFrameId,
                interactive = true,
                frameLabels = frameLabels,
                onDragStart = onDragStart,
                onDrag = onDrag,
                onDragEnd = onDragEnd,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 186.dp)
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color.White.copy(alpha = 0.96f),
            border = BorderStroke(1.dp, SurfaceMuted),
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Readout(
                        label = stringResource(R.string.readout_spread),
                        value = state.spreadLabel.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    VerticalRule()
                    Readout(
                        label = stringResource(R.string.readout_gap),
                        value = state.gapLabel.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    VerticalRule()
                    Readout(
                        label = stringResource(R.string.readout_frames),
                        value = state.frameCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (state.issue != ArrangementIssue.None) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.issue_off_wall),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 12.sp,
                        color = CoralAccent
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryButton(
                        text = stringResource(R.string.recentre),
                        onClick = onRecentre
                    )
                    Spacer(Modifier.width(12.dp))
                    PrimaryButton(
                        text = stringResource(R.string.calculate_nails),
                        onClick = onCalculate,
                        icon = AppIcons.Place,
                        enabled = state.canCalculate,
                        height = 56.dp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun Readout(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            letterSpacing = 1.4.sp,
            color = TextSecondary
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = InkDeep
        )
    }
}

@Composable
private fun VerticalRule() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(26.dp)
            .background(SurfaceMuted)
    )
}
