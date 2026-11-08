package com.gallerywall.spacer.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallerywall.spacer.core.config.GameConfig
import com.gallerywall.spacer.core.ui.MiniNail
import com.gallerywall.spacer.data.sample.SampleData
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.domain.repository.PlanRepository
import com.gallerywall.spacer.domain.repository.SettingsRepository
import com.gallerywall.spacer.domain.repository.WallRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

sealed interface MenuEvent {

    data object OpenSetup : MenuEvent

    data object OpenGame : MenuEvent
}

class MenuViewModel(
    private val wallRepository: WallRepository,
    private val planRepository: PlanRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val uiState = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = uiState.asStateFlow()

    private val events = Channel<MenuEvent>(Channel.BUFFERED)
    val eventFlow = events.receiveAsFlow()

    init {
        viewModelScope.launch {
            wallRepository.walls.collect { walls ->
                uiState.value = MenuUiState(
                    recentWalls = walls.take(GameConfig.MAX_LIBRARY_ROWS).map { saved ->
                        MenuWallItem(
                            id = saved.id,
                            name = saved.wall.name,
                            widthCm = saved.wall.widthCm.roundToInt(),
                            heightCm = saved.wall.heightCm.roundToInt(),
                            frameCount = saved.frames.size,
                            preview = saved.nails.map { nail ->
                                MiniNail(
                                    x = (nail.xCm / saved.wall.widthCm).coerceIn(0f, 1f),
                                    y = (1f - nail.yCm / saved.wall.heightCm).coerceIn(0f, 1f)
                                )
                            }
                        )
                    },
                    wallCount = walls.size,
                    totalNails = walls.sumOf { it.nails.size }
                )
            }
        }
    }

    fun startPlanning() {
        val settings = settingsRepository.settings.value
        planRepository.update {
            it.copy(
                savedWallId = null,
                wall = SampleData.defaultWall,
                frames = SampleData.defaultFrames,
                placements = emptyList(),
                layoutStyle = LayoutStyle.ROW,
                gapCm = settings.defaultGapCm,
                eyeHeightCm = settings.eyeHeightCm
            )
        }
        viewModelScope.launch { events.send(MenuEvent.OpenSetup) }
    }

    fun openWall(id: String) {
        val saved = wallRepository.byId(id) ?: return
        planRepository.update {
            it.copy(
                savedWallId = saved.id,
                wall = saved.wall,
                frames = saved.frames,
                placements = saved.placements,
                layoutStyle = saved.layoutStyle,
                gapCm = saved.gapCm,
                eyeHeightCm = saved.eyeHeightCm
            )
        }
        viewModelScope.launch { events.send(MenuEvent.OpenGame) }
    }
}
