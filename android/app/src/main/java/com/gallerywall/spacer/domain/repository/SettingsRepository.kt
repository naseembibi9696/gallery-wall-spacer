package com.gallerywall.spacer.domain.repository

import com.gallerywall.spacer.domain.model.AppSettings
import com.gallerywall.spacer.domain.model.UnitSystem
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val settings: StateFlow<AppSettings>

    fun setEyeHeight(valueCm: Float)

    fun setDefaultGap(valueCm: Float)

    fun setUnitSystem(unit: UnitSystem)

    fun resetToDefaults()
}
