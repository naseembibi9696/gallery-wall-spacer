package com.gallerywall.spacer.presentation.settings

import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.UnitSystem

data class SettingsUiState(
    val eyeHeightCm: Float = 145f,
    val eyeHeightLabel: String = "",
    val defaultGapCm: Int = 6,
    val unitSystem: UnitSystem = UnitSystem.CM,
    val previewWallWidthCm: Float = 320f,
    val previewWallHeightCm: Float = 260f,
    val previewFrames: List<FrameSpec> = emptyList(),
    val previewPlacements: List<PlacedFrame> = emptyList()
)
