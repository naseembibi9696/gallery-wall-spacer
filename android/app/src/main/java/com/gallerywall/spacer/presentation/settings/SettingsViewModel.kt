package com.gallerywall.spacer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallerywall.spacer.core.config.GameConfig
import com.gallerywall.spacer.domain.model.AppSettings
import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.domain.model.UnitSystem
import com.gallerywall.spacer.domain.model.WallSpec
import com.gallerywall.spacer.domain.repository.SettingsRepository
import com.gallerywall.spacer.domain.usecase.FormatMeasurementUseCase
import com.gallerywall.spacer.domain.usecase.SolveArrangementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val solveArrangement: SolveArrangementUseCase,
    private val formatMeasurement: FormatMeasurementUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { uiState.value = build(it) }
        }
    }

    fun setEyeHeight(valueCm: Float) {
        settingsRepository.setEyeHeight(valueCm)
    }

    fun adjustGap(step: Int) {
        settingsRepository.setDefaultGap(
            settingsRepository.settings.value.defaultGapCm + step * GameConfig.GAP_STEP_CM
        )
    }

    fun setUnitSystem(unit: UnitSystem) {
        settingsRepository.setUnitSystem(unit)
    }

    fun resetToDefaults() {
        settingsRepository.resetToDefaults()
    }

    private fun build(settings: AppSettings): SettingsUiState {
        val placements = solveArrangement(
            PREVIEW_WALL,
            PREVIEW_FRAMES,
            LayoutStyle.ROW,
            settings.defaultGapCm,
            settings.eyeHeightCm
        )
        return SettingsUiState(
            eyeHeightCm = settings.eyeHeightCm,
            eyeHeightLabel = formatMeasurement(settings.eyeHeightCm, settings.unitSystem),
            defaultGapCm = settings.defaultGapCm.roundToInt(),
            unitSystem = settings.unitSystem,
            previewWallWidthCm = PREVIEW_WALL.widthCm,
            previewWallHeightCm = PREVIEW_WALL.heightCm,
            previewFrames = PREVIEW_FRAMES,
            previewPlacements = placements
        )
    }

    private companion object {
        val PREVIEW_WALL = WallSpec(
            name = "Preview",
            widthCm = 200f,
            heightCm = 240f,
            furnitureTopCm = 0f
        )
        val PREVIEW_FRAMES = listOf(
            FrameSpec(id = 1, widthCm = 50f, heightCm = 60f, hangerDropCm = 5f, spriteIndex = 0)
        )
    }
}
