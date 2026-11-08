package com.gallerywall.spacer.domain.model

enum class SnapAxis {
    VERTICAL,
    HORIZONTAL
}

data class SnapGuide(
    val axis: SnapAxis,
    val positionCm: Float
)
