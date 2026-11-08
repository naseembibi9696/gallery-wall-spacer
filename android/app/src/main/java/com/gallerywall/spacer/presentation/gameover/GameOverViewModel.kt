package com.gallerywall.spacer.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallerywall.spacer.domain.model.AppSettings
import com.gallerywall.spacer.domain.model.SavedWall
import com.gallerywall.spacer.domain.model.UnitSystem
import com.gallerywall.spacer.domain.repository.PlanRepository
import com.gallerywall.spacer.domain.repository.SettingsRepository
import com.gallerywall.spacer.domain.repository.WallRepository
import com.gallerywall.spacer.domain.usecase.FormatMeasurementUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface GameOverEvent {

    data object OpenSetup : GameOverEvent
}

class GameOverViewModel(
    private val wallRepository: WallRepository,
    private val planRepository: PlanRepository,
    settingsRepository: SettingsRepository,
    private val formatMeasurement: FormatMeasurementUseCase
) : ViewModel() {

    private val selectedId = MutableStateFlow("")

    private val uiState = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = uiState.asStateFlow()

    private val events = Channel<GameOverEvent>(Channel.BUFFERED)
    val eventFlow = events.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                wallRepository.walls,
                settingsRepository.settings,
                selectedId
            ) { walls, settings, id ->
                build(walls.firstOrNull { it.id == id }, settings)
            }.collect { uiState.value = it }
        }
    }

    fun select(planId: String) {
        selectedId.value = planId
    }

    fun toggleNail(index: Int) {
        val saved = wallRepository.byId(selectedId.value) ?: return
        wallRepository.save(
            saved.copy(
                nails = saved.nails.map { nail ->
                    if (nail.index == index) nail.copy(done = !nail.done) else nail
                }
            )
        )
    }

    fun planAgain() {
        val saved = wallRepository.byId(selectedId.value)
        if (saved != null) {
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
        }
        viewModelScope.launch { events.send(GameOverEvent.OpenSetup) }
    }

    private fun build(saved: SavedWall?, settings: AppSettings): GameOverUiState {
        if (saved == null) return GameOverUiState(inchUnits = settings.unitSystem == UnitSystem.INCH)
        val unit = settings.unitSystem
        val byId = saved.frames.associateBy { it.id }
        var left = saved.wall.widthCm
        var right = 0f
        saved.placements.forEach { placed ->
            val frame = byId[placed.frameId]
            if (frame != null) {
                left = minOf(left, placed.xCm)
                right = maxOf(right, placed.xCm + frame.widthCm)
            }
        }
        val done = saved.nails.count { it.done }
        val total = saved.nails.size
        return GameOverUiState(
            wallName = saved.wall.name,
            nails = saved.nails.map { nail ->
                NailRowItem(
                    index = nail.index,
                    xLabel = formatMeasurement(nail.xCm, unit),
                    yLabel = formatMeasurement(nail.yCm, unit),
                    done = nail.done
                )
            },
            centrelineLabel = formatMeasurement(saved.eyeHeightCm, unit),
            spreadLabel = formatMeasurement((right - left).coerceAtLeast(0f), unit),
            doneCount = done,
            totalCount = total,
            progress = if (total == 0) 0f else done.toFloat() / total.toFloat(),
            complete = total > 0 && done == total,
            empty = total == 0,
            inchUnits = unit == UnitSystem.INCH
        )
    }
}
