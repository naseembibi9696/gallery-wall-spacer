package com.gallerywall.spacer.presentation.menu

import com.gallerywall.spacer.core.ui.MiniNail

data class MenuWallItem(
    val id: String,
    val name: String,
    val widthCm: Int,
    val heightCm: Int,
    val frameCount: Int,
    val preview: List<MiniNail>
)

data class MenuUiState(
    val recentWalls: List<MenuWallItem> = emptyList(),
    val wallCount: Int = 0,
    val totalNails: Int = 0
)
