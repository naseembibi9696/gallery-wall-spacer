package com.gallerywall.spacer.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallerywall.spacer.R
import com.gallerywall.spacer.core.config.GameConfig
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
import com.gallerywall.spacer.core.ui.FrameLabel
import com.gallerywall.spacer.core.ui.SectionHeader
import com.gallerywall.spacer.core.ui.StepperRow
import com.gallerywall.spacer.core.ui.WallElevation
import com.gallerywall.spacer.domain.model.UnitSystem

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsContent(
        state = state,
        onBack = onBack,
        onEyeHeight = viewModel::setEyeHeight,
        onAdjustGap = viewModel::adjustGap,
        onUnitSystem = viewModel::setUnitSystem,
        onReset = viewModel::resetToDefaults
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onEyeHeight: (Float) -> Unit,
    onAdjustGap: (Int) -> Unit,
    onUnitSystem: (UnitSystem) -> Unit,
    onReset: () -> Unit
) {
    val minusLabel = stringResource(R.string.cd_decrease)
    val plusLabel = stringResource(R.string.cd_increase)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(PlasterBg, PlasterBgDeep, PlasterBgDeepest))
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
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(InkPrimary.copy(alpha = 0.06f), PlasterBg.copy(alpha = 0f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                    com.gallerywall.spacer.core.ui.BackGlyph(color = InkPrimary)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 13.sp,
                    letterSpacing = 2.2.sp,
                    color = InkPrimary
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.size(48.dp))
            }
            HorizontalDivider(thickness = 1.dp, color = SurfaceMuted)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (state.unitSystem == UnitSystem.INCH) {
                        stringResource(R.string.value_inch, state.eyeHeightLabel)
                    } else {
                        stringResource(R.string.value_cm, state.eyeHeightLabel)
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light,
                    color = InkDeep,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite }
                )
                Spacer(Modifier.height(12.dp))
                WallElevation(
                    wallWidthCm = state.previewWallWidthCm,
                    wallHeightCm = state.previewWallHeightCm,
                    furnitureTopCm = 0f,
                    eyeHeightCm = state.eyeHeightCm,
                    eyeLabel = stringResource(
                        R.string.eye_label,
                        state.eyeHeightCm.toInt()
                    ),
                    floorLabel = stringResource(R.string.floor),
                    frames = state.previewFrames,
                    placements = state.previewPlacements,
                    guides = emptyList(),
                    selectedFrameId = -1,
                    invalidFrameId = -1,
                    interactive = false,
                    frameLabels = emptyMap<Int, FrameLabel>(),
                    onDragStart = {},
                    onDrag = { _, _, _ -> },
                    onDragEnd = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(Modifier.height(16.dp))
                Slider(
                    value = state.eyeHeightCm,
                    onValueChange = onEyeHeight,
                    valueRange = GameConfig.MIN_EYE_HEIGHT_CM..GameConfig.MAX_EYE_HEIGHT_CM,
                    steps = 49,
                    colors = SliderDefaults.colors(
                        thumbColor = CoralAccent,
                        activeTrackColor = InkPrimary,
                        inactiveTrackColor = SurfaceMuted
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )
                Text(
                    text = stringResource(R.string.settings_caption),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(Modifier.height(22.dp))
                SectionHeader(label = stringResource(R.string.default_gap))
                StepperRow(
                    label = stringResource(R.string.label_gap),
                    value = stringResource(R.string.gap_value, state.defaultGapCm),
                    decrementLabel = minusLabel,
                    incrementLabel = plusLabel,
                    onDecrement = { onAdjustGap(-1) },
                    onIncrement = { onAdjustGap(1) }
                )

                Spacer(Modifier.height(22.dp))
                SectionHeader(label = stringResource(R.string.units))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    UnitCard(
                        label = stringResource(R.string.unit_cm),
                        selected = state.unitSystem == UnitSystem.CM,
                        onClick = { onUnitSystem(UnitSystem.CM) },
                        modifier = Modifier.weight(1f)
                    )
                    UnitCard(
                        label = stringResource(R.string.unit_inch),
                        selected = state.unitSystem == UnitSystem.INCH,
                        onClick = { onUnitSystem(UnitSystem.INCH) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(thickness = 1.dp, color = SurfaceMuted)
                TextButton(
                    onClick = onReset,
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.reset_defaults),
                        style = MaterialTheme.typography.labelLarge,
                        color = CoralAccent
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun UnitCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) InkPrimary else SurfaceCard,
        border = BorderStroke(1.dp, if (selected) InkPrimary else SurfaceMuted)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 13.sp,
                color = if (selected) PlasterBg else InkPrimary
            )
            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(CoralAccent)
                )
            }
        }
    }
}
