package com.gallerywall.spacer.presentation.setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallerywall.spacer.R
import com.gallerywall.spacer.core.di.AppViewModelFactory
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.core.theme.PlasterBgDeep
import com.gallerywall.spacer.core.theme.PlasterBgDeepest
import com.gallerywall.spacer.core.theme.SurfaceCard
import com.gallerywall.spacer.core.theme.SurfaceMuted
import com.gallerywall.spacer.core.theme.TextSecondary
import com.gallerywall.spacer.core.ui.AddFrameChip
import com.gallerywall.spacer.core.ui.BackGlyph
import com.gallerywall.spacer.core.ui.FrameChip
import com.gallerywall.spacer.core.ui.MeasureField
import com.gallerywall.spacer.core.ui.PrimaryButton
import com.gallerywall.spacer.core.ui.SectionHeader
import com.gallerywall.spacer.core.ui.StepperRow
import com.gallerywall.spacer.domain.model.ArrangementIssue
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.presentation.dialog.AddFrameDialog

@Composable
fun SetupScreen(
    onBack: () -> Unit,
    onOpenGame: () -> Unit,
    viewModel: SetupViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                SetupEvent.OpenGame -> onOpenGame()
            }
        }
    }
    SetupContent(
        state = state,
        onBack = onBack,
        onFocus = viewModel::focus,
        onAdjustMeasure = viewModel::adjustMeasure,
        onSelectFrame = viewModel::selectFrame,
        onAdjustFrameSize = viewModel::adjustFrameSize,
        onAdjustFrameDrop = viewModel::adjustFrameDrop,
        onRemoveFrame = viewModel::removeSelectedFrame,
        onAddFrame = viewModel::openAddFrameDialog,
        onDismissDialog = viewModel::dismissAddFrameDialog,
        onDraftWidth = viewModel::adjustDraftWidth,
        onDraftHeight = viewModel::adjustDraftHeight,
        onDraftDrop = viewModel::adjustDraftDrop,
        onConfirmFrame = viewModel::confirmAddFrame,
        onSetLayout = viewModel::setLayoutStyle,
        onAdjustGap = viewModel::adjustGap,
        onPlaceFrames = viewModel::placeFrames
    )
}

@Composable
private fun SetupContent(
    state: SetupUiState,
    onBack: () -> Unit,
    onFocus: (MeasureTarget) -> Unit,
    onAdjustMeasure: (Int) -> Unit,
    onSelectFrame: (Int) -> Unit,
    onAdjustFrameSize: (Int) -> Unit,
    onAdjustFrameDrop: (Int) -> Unit,
    onRemoveFrame: () -> Unit,
    onAddFrame: () -> Unit,
    onDismissDialog: () -> Unit,
    onDraftWidth: (Int) -> Unit,
    onDraftHeight: (Int) -> Unit,
    onDraftDrop: (Int) -> Unit,
    onConfirmFrame: () -> Unit,
    onSetLayout: (LayoutStyle) -> Unit,
    onAdjustGap: (Int) -> Unit,
    onPlaceFrames: () -> Unit
) {
    val minusLabel = stringResource(R.string.cd_decrease)
    val plusLabel = stringResource(R.string.cd_increase)
    val selectedLabel = stringResource(R.string.state_selected)
    val notSelectedLabel = stringResource(R.string.state_not_selected)

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
                .height(180.dp)
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
                    BackGlyph(color = InkPrimary)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.setup_title),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 13.sp,
                    letterSpacing = 2.2.sp,
                    color = InkPrimary
                )
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_pause),
                        contentDescription = stringResource(R.string.cd_save_draft),
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp, color = SurfaceMuted)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                SectionHeader(
                    label = stringResource(R.string.section_wall_size),
                    showRule = false
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MeasureField(
                        label = stringResource(R.string.field_width),
                        value = state.widthCm.toString(),
                        focused = state.focused == MeasureTarget.WIDTH,
                        stateLabel = if (state.focused == MeasureTarget.WIDTH) {
                            selectedLabel
                        } else {
                            notSelectedLabel
                        },
                        onFocus = { onFocus(MeasureTarget.WIDTH) },
                        modifier = Modifier.weight(1f)
                    )
                    MeasureField(
                        label = stringResource(R.string.field_height),
                        value = state.heightCm.toString(),
                        focused = state.focused == MeasureTarget.HEIGHT,
                        stateLabel = if (state.focused == MeasureTarget.HEIGHT) {
                            selectedLabel
                        } else {
                            notSelectedLabel
                        },
                        onFocus = { onFocus(MeasureTarget.HEIGHT) },
                        modifier = Modifier.weight(1f)
                    )
                    MeasureField(
                        label = stringResource(R.string.field_furniture),
                        value = state.furnitureTopCm.toString(),
                        focused = state.focused == MeasureTarget.FURNITURE,
                        stateLabel = if (state.focused == MeasureTarget.FURNITURE) {
                            selectedLabel
                        } else {
                            notSelectedLabel
                        },
                        onFocus = { onFocus(MeasureTarget.FURNITURE) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                StepperRow(
                    label = stringResource(R.string.label_adjust),
                    value = stringResource(
                        R.string.value_cm,
                        when (state.focused) {
                            MeasureTarget.WIDTH -> state.widthCm.toString()
                            MeasureTarget.HEIGHT -> state.heightCm.toString()
                            MeasureTarget.FURNITURE -> state.furnitureTopCm.toString()
                        }
                    ),
                    decrementLabel = minusLabel,
                    incrementLabel = plusLabel,
                    onDecrement = { onAdjustMeasure(-1) },
                    onIncrement = { onAdjustMeasure(1) }
                )

                Spacer(Modifier.height(20.dp))
                SectionHeader(label = stringResource(R.string.section_frame_tray))
                LazyRow(
                    modifier = Modifier.height(140.dp),
                    contentPadding = PaddingValues(end = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.frames.size) { index ->
                        val item = state.frames[index]
                        FrameChip(
                            sizeLabel = stringResource(
                                R.string.frame_size,
                                item.widthCm,
                                item.heightCm
                            ),
                            dropLabel = stringResource(R.string.frame_drop, item.dropCm),
                            spriteIndex = item.spriteIndex,
                            selected = item.selected,
                            stateLabel = if (item.selected) selectedLabel else notSelectedLabel,
                            onClick = { onSelectFrame(item.id) }
                        )
                    }
                    item {
                        AddFrameChip(
                            label = stringResource(R.string.add_frame),
                            onClick = onAddFrame
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                StepperRow(
                    label = stringResource(R.string.label_size),
                    value = stringResource(
                        R.string.frame_size,
                        state.selectedWidthCm,
                        state.selectedHeightCm
                    ),
                    decrementLabel = minusLabel,
                    incrementLabel = plusLabel,
                    onDecrement = { onAdjustFrameSize(-1) },
                    onIncrement = { onAdjustFrameSize(1) }
                )
                Spacer(Modifier.height(8.dp))
                StepperRow(
                    label = stringResource(R.string.label_drop),
                    value = stringResource(R.string.value_cm, state.selectedDropCm.toString()),
                    decrementLabel = minusLabel,
                    incrementLabel = plusLabel,
                    onDecrement = { onAdjustFrameDrop(-1) },
                    onIncrement = { onAdjustFrameDrop(1) }
                )
                Spacer(Modifier.height(4.dp))
                TextButton(
                    onClick = onRemoveFrame,
                    enabled = state.canRemoveFrame,
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.remove),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (state.canRemoveFrame) {
                            CoralAccent
                        } else {
                            CoralAccent.copy(alpha = 0.35f)
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))
                SectionHeader(label = stringResource(R.string.section_layout_style))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LayoutCard(
                        label = stringResource(R.string.layout_row),
                        style = LayoutStyle.ROW,
                        selected = state.layoutStyle == LayoutStyle.ROW,
                        onClick = { onSetLayout(LayoutStyle.ROW) },
                        modifier = Modifier.weight(1f)
                    )
                    LayoutCard(
                        label = stringResource(R.string.layout_grid),
                        style = LayoutStyle.GRID,
                        selected = state.layoutStyle == LayoutStyle.GRID,
                        onClick = { onSetLayout(LayoutStyle.GRID) },
                        modifier = Modifier.weight(1f)
                    )
                    LayoutCard(
                        label = stringResource(R.string.layout_cluster),
                        style = LayoutStyle.CLUSTER,
                        selected = state.layoutStyle == LayoutStyle.CLUSTER,
                        onClick = { onSetLayout(LayoutStyle.CLUSTER) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                StepperRow(
                    label = stringResource(R.string.label_gap),
                    value = stringResource(R.string.gap_value, state.gapCm),
                    decrementLabel = minusLabel,
                    incrementLabel = plusLabel,
                    onDecrement = { onAdjustGap(-1) },
                    onIncrement = { onAdjustGap(1) }
                )
                Spacer(Modifier.height(20.dp))
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                IssueLine(issue = state.issue)
                PrimaryButton(
                    text = stringResource(R.string.place_frames),
                    onClick = onPlaceFrames,
                    enabled = state.canPlace,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (state.dialogVisible) {
        AddFrameDialog(
            widthCm = state.draftWidthCm,
            heightCm = state.draftHeightCm,
            dropCm = state.draftDropCm,
            onWidthChange = onDraftWidth,
            onHeightChange = onDraftHeight,
            onDropChange = onDraftDrop,
            onConfirm = onConfirmFrame,
            onDismiss = onDismissDialog
        )
    }
}

@Composable
private fun IssueLine(issue: ArrangementIssue) {
    val message = when (issue) {
        ArrangementIssue.None -> null
        ArrangementIssue.TooWide -> stringResource(R.string.issue_too_wide)
        ArrangementIssue.AboveCeiling -> stringResource(R.string.issue_above_ceiling)
        ArrangementIssue.OverFurniture -> stringResource(R.string.issue_over_furniture)
        is ArrangementIssue.NailOffWall -> stringResource(R.string.issue_nail_off_wall)
    }
    if (message != null) {
        Text(
            text = message,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 12.sp,
            color = CoralAccent
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun LayoutCard(
    label: String,
    style: LayoutStyle,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) InkPrimary else SurfaceCard,
        border = BorderStroke(1.dp, if (selected) InkPrimary else SurfaceMuted)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LayoutGlyph(style = style, selected = selected)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 12.sp,
                    color = if (selected) PlasterBg else InkPrimary
                )
            }
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

@Composable
private fun LayoutGlyph(style: LayoutStyle, selected: Boolean) {
    val tint = if (selected) PlasterBg else TextSecondary
    Canvas(modifier = Modifier.size(26.dp, 12.dp)) {
        val r = 2.dp.toPx()
        val points = when (style) {
            LayoutStyle.ROW -> listOf(
                Offset(size.width * 0.2f, size.height * 0.5f),
                Offset(size.width * 0.5f, size.height * 0.5f),
                Offset(size.width * 0.8f, size.height * 0.5f)
            )

            LayoutStyle.GRID -> listOf(
                Offset(size.width * 0.35f, size.height * 0.25f),
                Offset(size.width * 0.65f, size.height * 0.25f),
                Offset(size.width * 0.35f, size.height * 0.75f),
                Offset(size.width * 0.65f, size.height * 0.75f)
            )

            LayoutStyle.CLUSTER -> listOf(
                Offset(size.width * 0.25f, size.height * 0.3f),
                Offset(size.width * 0.55f, size.height * 0.65f),
                Offset(size.width * 0.75f, size.height * 0.25f),
                Offset(size.width * 0.45f, size.height * 0.2f)
            )
        }
        points.forEach { drawCircle(color = tint, radius = r, center = it) }
    }
}
