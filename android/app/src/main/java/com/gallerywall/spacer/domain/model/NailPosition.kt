package com.gallerywall.spacer.domain.model

data class NailPosition(
    val frameId: Int,
    val index: Int,
    val xCm: Float,
    val yCm: Float,
    val done: Boolean = false
)
