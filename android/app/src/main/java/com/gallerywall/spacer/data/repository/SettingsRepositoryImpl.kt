package com.gallerywall.spacer.data.repository

import com.gallerywall.spacer.core.config.GameConfig
import com.gallerywall.spacer.data.local.SettingsPreferences
import com.gallerywall.spacer.domain.model.AppSettings
import com.gallerywall.spacer.domain.model.UnitSystem
import com.gallerywall.spacer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val preferences: SettingsPreferences
) : SettingsRepository {

    private val state = MutableStateFlow(preferences.load())

    override val settings: StateFlow<AppSettings> = state.asStateFlow()

    override fun setEyeHeight(valueCm: Float) {
        write(
            state.value.copy(
                eyeHeightCm = valueCm.coerceIn(
                    GameConfig.MIN_EYE_HEIGHT_CM,
                    GameConfig.MAX_EYE_HEIGHT_CM
                )
            )
        )
    }

    override fun setDefaultGap(valueCm: Float) {
        write(
            state.value.copy(
                defaultGapCm = valueCm.coerceIn(GameConfig.MIN_GAP_CM, GameConfig.MAX_GAP_CM)
            )
        )
    }

    override fun setUnitSystem(unit: UnitSystem) {
        write(state.value.copy(unitSystem = unit))
    }

    override fun resetToDefaults() {
        write(
            AppSettings(
                eyeHeightCm = GameConfig.DEFAULT_EYE_HEIGHT_CM,
                defaultGapCm = GameConfig.DEFAULT_GAP_CM,
                unitSystem = UnitSystem.CM
            )
        )
    }

    private fun write(settings: AppSettings) {
        state.value = settings
        preferences.store(settings)
    }
}
