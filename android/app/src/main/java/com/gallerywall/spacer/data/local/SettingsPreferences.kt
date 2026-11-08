package com.gallerywall.spacer.data.local

import android.content.Context
import com.gallerywall.spacer.core.config.GameConfig
import com.gallerywall.spacer.domain.model.AppSettings
import com.gallerywall.spacer.domain.model.UnitSystem

class SettingsPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun load(): AppSettings = AppSettings(
        eyeHeightCm = prefs.getFloat(KEY_EYE, GameConfig.DEFAULT_EYE_HEIGHT_CM),
        defaultGapCm = prefs.getFloat(KEY_GAP, GameConfig.DEFAULT_GAP_CM),
        unitSystem = if (prefs.getString(KEY_UNIT, UnitSystem.CM.name) == UnitSystem.INCH.name) {
            UnitSystem.INCH
        } else {
            UnitSystem.CM
        }
    )

    fun store(settings: AppSettings) {
        prefs.edit()
            .putFloat(KEY_EYE, settings.eyeHeightCm)
            .putFloat(KEY_GAP, settings.defaultGapCm)
            .putString(KEY_UNIT, settings.unitSystem.name)
            .apply()
    }

    private companion object {
        const val FILE_NAME = "gallery_wall_settings"
        const val KEY_EYE = "eye_height"
        const val KEY_GAP = "default_gap"
        const val KEY_UNIT = "unit_system"
    }
}
