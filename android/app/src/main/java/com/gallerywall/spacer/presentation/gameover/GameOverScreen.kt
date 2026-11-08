package com.gallerywall.spacer.presentation.gameover

import com.gallerywall.spacer.core.ui.AppIcons
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallerywall.spacer.R
import com.gallerywall.spacer.core.di.AppViewModelFactory
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkMid
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.ui.BackGlyph
import com.gallerywall.spacer.core.ui.NailRow
import com.gallerywall.spacer.core.ui.PrimaryButton
import com.gallerywall.spacer.core.ui.SecondaryButton
import com.gallerywall.spacer.core.ui.StatCard

@Composable
fun GameOverScreen(
    planId: String,
    onBack: () -> Unit,
    onOpenSetup: () -> Unit,
    onMenu: () -> Unit,
    viewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(planId) {
        viewModel.select(planId)
    }
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                GameOverEvent.OpenSetup -> onOpenSetup()
            }
        }
    }
    GameOverContent(
        state = state,
        onBack = onBack,
        onToggleNail = viewModel::toggleNail,
        onPlanAgain = viewModel::planAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onBack: () -> Unit,
    onToggleNail: (Int) -> Unit,
    onPlanAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "nailProgress"
    )
    val hungLabel = stringResource(R.string.state_hung)
    val pendingLabel = stringResource(R.string.state_pending)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(InkPrimary, InkMid, InkDeep)))
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.07f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                    BackGlyph(color = PlasterBg)
                }
                Spacer(Modifier.size(8.dp))
                Column {
                    Text(
                        text = stringResource(R.string.gameover_title),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 13.sp,
                        letterSpacing = 2.4.sp,
                        color = PlasterBg
                    )
                    Text(
                        text = stringResource(
                            R.string.gameover_subtitle,
                            state.totalCount,
                            state.wallName
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        letterSpacing = 0.6.sp,
                        color = SurfaceMuted.copy(alpha = 0.7f)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                if (!state.empty) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(
                            label = stringResource(R.string.stat_centreline),
                            value = if (state.inchUnits) {
                                stringResource(R.string.value_inch, state.centrelineLabel)
                            } else {
                                stringResource(R.string.value_cm, state.centrelineLabel)
                            },
                            dark = true,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_total_spread),
                            value = if (state.inchUnits) {
                                stringResource(R.string.value_inch, state.spreadLabel)
                            } else {
                                stringResource(R.string.value_cm, state.spreadLabel)
                            },
                            dark = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.hanging_order_note),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 11.sp,
                        color = SurfaceMuted.copy(alpha = 0.65f)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            if (state.empty) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.nothing_to_hang),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 13.sp,
                        color = SurfaceMuted,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.nails.size) { position ->
                        val nail = state.nails[position]
                        NailRow(
                            title = stringResource(R.string.nail_label, nail.index),
                            position = if (state.inchUnits) {
                                stringResource(
                                    R.string.nail_position_inch,
                                    nail.xLabel,
                                    nail.yLabel
                                )
                            } else {
                                stringResource(R.string.nail_position_cm, nail.xLabel, nail.yLabel)
                            },
                            index = nail.index,
                            done = nail.done,
                            stateLabel = if (nail.done) hungLabel else pendingLabel,
                            onToggle = { onToggleNail(nail.index) }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                if (!state.empty) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(
                            R.string.progress_hung,
                            state.doneCount,
                            state.totalCount
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        letterSpacing = 1.6.sp,
                        color = SurfaceMuted,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = CoralAccent,
                        trackColor = Color.White.copy(alpha = 0.16f)
                    )
                    if (state.complete) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.all_nails_hung),
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 13.sp,
                            color = PlasterBg
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = stringResource(R.string.plan_again),
                    onClick = onPlanAgain,
                    icon = AppIcons.Refresh,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    light = false,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
