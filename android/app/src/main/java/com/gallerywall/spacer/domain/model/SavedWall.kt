package com.gallerywall.spacer.domain.model

data class SavedWall(
    val id: String,
    val wall: WallSpec,
    val frames: List<FrameSpec>,
    val placements: List<PlacedFrame>,
    val nails: List<NailPosition>,
    val layoutStyle: LayoutStyle,
    val gapCm: Float,
    val eyeHeightCm: Float
)
