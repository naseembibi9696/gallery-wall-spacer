package com.gallerywall.spacer.presentation.game

import com.gallerywall.spacer.domain.model.ArrangementIssue
import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.SnapGuide

data class GameUiState(
    val wallWidthCm: Float = 320f,
    val wallHeightCm: Float = 260f,
    val furnitureTopCm: Float = 78f,
    val eyeHeightCm: Float = 145f,
    val wallWidthLabel: Int = 320,
    val wallHeightLabel: Int = 260,
    val eyeHeightLabel: Int = 145,
    val frames: List<FrameSpec> = emptyList(),
    val placements: List<PlacedFrame> = emptyList(),
    val guides: List<SnapGuide> = emptyList(),
    val selectedFrameId: Int = -1,
    val invalidFrameId: Int = -1,
    val dragging: Boolean = false,
    val spreadLabel: Int = 0,
    val gapLabel: Int = 0,
    val frameCount: Int = 0,
    val issue: ArrangementIssue = ArrangementIssue.None,
    val canCalculate: Boolean = true
)
