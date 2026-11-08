package com.gallerywall.spacer.presentation.gameover

data class NailRowItem(
    val index: Int,
    val xLabel: String,
    val yLabel: String,
    val done: Boolean
)

data class GameOverUiState(
    val wallName: String = "",
    val nails: List<NailRowItem> = emptyList(),
    val centrelineLabel: String = "",
    val spreadLabel: String = "",
    val doneCount: Int = 0,
    val totalCount: Int = 0,
    val progress: Float = 0f,
    val complete: Boolean = false,
    val empty: Boolean = true,
    val inchUnits: Boolean = false
)
