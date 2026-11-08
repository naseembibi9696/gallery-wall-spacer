package com.gallerywall.spacer.presentation.setup

import com.gallerywall.spacer.domain.model.ArrangementIssue
import com.gallerywall.spacer.domain.model.LayoutStyle

enum class MeasureTarget {
    WIDTH,
    HEIGHT,
    FURNITURE
}

data class FrameChipItem(
    val id: Int,
    val widthCm: Int,
    val heightCm: Int,
    val dropCm: Int,
    val spriteIndex: Int,
    val selected: Boolean
)

data class SetupUiState(
    val widthCm: Int = 320,
    val heightCm: Int = 260,
    val furnitureTopCm: Int = 78,
    val focused: MeasureTarget = MeasureTarget.WIDTH,
    val frames: List<FrameChipItem> = emptyList(),
    val selectedFrameId: Int = 0,
    val selectedWidthCm: Int = 0,
    val selectedHeightCm: Int = 0,
    val selectedDropCm: Int = 0,
    val layoutStyle: LayoutStyle = LayoutStyle.ROW,
    val gapCm: Int = 6,
    val canRemoveFrame: Boolean = false,
    val issue: ArrangementIssue = ArrangementIssue.None,
    val canPlace: Boolean = true,
    val dialogVisible: Boolean = false,
    val draftWidthCm: Int = 30,
    val draftHeightCm: Int = 40,
    val draftDropCm: Int = 3
)
