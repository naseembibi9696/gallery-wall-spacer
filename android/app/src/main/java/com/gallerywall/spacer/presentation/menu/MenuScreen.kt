package com.gallerywall.spacer.presentation.menu

import com.gallerywall.spacer.core.ui.AppIcons
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.gallerywall.spacer.core.theme.PlasterBgDeepest
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary
import com.gallerywall.spacer.core.ui.PrimaryButton
import com.gallerywall.spacer.core.ui.StatCard
import com.gallerywall.spacer.core.ui.WallRow
import com.gallerywall.spacer.core.ui.spriteFor

@Composable
fun MenuScreen(
    onOpenSetup: () -> Unit,
    onOpenGame: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                MenuEvent.OpenSetup -> onOpenSetup()
                MenuEvent.OpenGame -> onOpenGame()
            }
        }
    }
    MenuContent(
        state = state,
        onStart = viewModel::startPlanning,
        onOpenWall = viewModel::openWall,
        onOpenSettings = onOpenSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onStart: () -> Unit,
    onOpenWall: (String) -> Unit,
    onOpenSettings: () -> Unit
) {
    val entry = remember { MutableTransitionState(false).apply { targetState = true } }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(PlasterBg, PlasterBgDeep, PlasterBgDeepest)
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.10f),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(InkPrimary.copy(alpha = 0.05f), PlasterBg.copy(alpha = 0f)),
                        radius = 520f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_home),
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.menu_eyebrow),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = InkPrimary.copy(alpha = 0.7f)
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.Settings,
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = InkPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                AnimatedVisibility(
                    visibleState = entry,
                    enter = fadeIn(tween(420)) + slideInVertically(tween(420)) { it / 4 }
                ) {
                    Column {
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = stringResource(R.string.menu_title_line1),
                            style = MaterialTheme.typography.displaySmall,
                            color = InkDeep
                        )
                        Text(
                            text = stringResource(R.string.menu_title_line2),
                            style = MaterialTheme.typography.displaySmall,
                            color = InkDeep
                        )
                        Text(
                            text = stringResource(R.string.menu_title_line3),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = InkPrimary
                        )
                        Spacer(Modifier.height(14.dp))
                        HorizontalDivider(
                            modifier = Modifier.width(96.dp),
                            thickness = 1.dp,
                            color = SurfaceMuted
                        )
                        Spacer(Modifier.height(14.dp))
                        Text(
                            text = stringResource(R.string.menu_tagline),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            modifier = Modifier.widthIn(max = 300.dp)
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                AnimatedVisibility(
                    visibleState = entry,
                    enter = fadeIn(tween(520)) +
                        scaleIn(
                            initialScale = 0.94f,
                            animationSpec = spring(dampingRatio = 0.8f, stiffness = 260f)
                        )
                ) {
                    HeroTrio()
                }

                Spacer(Modifier.height(20.dp))

                if (state.recentWalls.isEmpty()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceCard.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, SurfaceMuted)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(R.string.menu_empty),
                                style = MaterialTheme.typography.labelSmall,
                                color = InkPrimary.copy(alpha = 0.5f)
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        state.recentWalls.forEach { item ->
                            WallRow(
                                name = item.name,
                                summary = stringResource(
                                    R.string.wall_summary,
                                    item.widthCm,
                                    item.heightCm,
                                    item.frameCount
                                ),
                                preview = item.preview,
                                openLabel = stringResource(R.string.state_saved_wall),
                                onClick = { onOpenWall(item.id) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.labelMedium,
                color = InkPrimary.copy(alpha = 0.58f)
            )

            Spacer(Modifier.height(12.dp))

            val readyLabel = stringResource(R.string.state_ready)
            PrimaryButton(
                text = stringResource(R.string.start_planning),
                onClick = onStart,
                icon = AppIcons.PlayArrow,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { stateDescription = readyLabel }
            )

            if (state.wallCount > 0 && state.totalNails > 0) {
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_walls_saved),
                        value = state.wallCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_nails_planned),
                        value = state.totalNails.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HeroTrio() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
    ) {
        MattedFrame(
            spriteIndex = 0,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 18.dp)
                .size(118.dp, 92.dp)
        )
        MattedFrame(
            spriteIndex = 1,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 18.dp, y = 36.dp)
                .size(86.dp, 114.dp)
        )
        MattedFrame(
            spriteIndex = 2,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = 62.dp)
                .size(96.dp, 96.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(y = 10.dp)
                .width(140.dp)
                .height(1.dp)
                .background(CoralAccent.copy(alpha = 0.55f))
        )
    }
}

@Composable
private fun MattedFrame(
    spriteIndex: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceMuted),
        shadowElevation = 3.dp
    ) {
        Image(
            painter = painterResource(spriteFor(spriteIndex)),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            contentScale = ContentScale.Fit
        )
    }
}
