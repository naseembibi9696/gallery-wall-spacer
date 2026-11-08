package com.gallerywall.spacer.domain.model

data class PlanDraft(
    val savedWallId: String?,
    val wall: WallSpec,
    val frames: List<FrameSpec>,
    val placements: List<PlacedFrame>,
    val layoutStyle: LayoutStyle,
    val gapCm: Float,
    val eyeHeightCm: Float
)
