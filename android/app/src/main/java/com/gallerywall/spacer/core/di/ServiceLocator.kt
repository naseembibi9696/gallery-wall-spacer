package com.gallerywall.spacer.core.di

import android.content.Context
import com.gallerywall.spacer.data.local.SettingsPreferences
import com.gallerywall.spacer.data.local.WallPreferences
import com.gallerywall.spacer.data.repository.PlanRepositoryImpl
import com.gallerywall.spacer.data.repository.SettingsRepositoryImpl
import com.gallerywall.spacer.data.repository.WallRepositoryImpl
import com.gallerywall.spacer.domain.repository.PlanRepository
import com.gallerywall.spacer.domain.repository.SettingsRepository
import com.gallerywall.spacer.domain.repository.WallRepository
import com.gallerywall.spacer.domain.usecase.ComputeNailPositionsUseCase
import com.gallerywall.spacer.domain.usecase.FormatMeasurementUseCase
import com.gallerywall.spacer.domain.usecase.SnapFrameUseCase
import com.gallerywall.spacer.domain.usecase.SolveArrangementUseCase
import com.gallerywall.spacer.domain.usecase.ValidateArrangementUseCase

object ServiceLocator {

    private var application: Context? = null

    private val context: Context
        get() = requireNotNull(application)

    val solveArrangement = SolveArrangementUseCase()
    val computeNails = ComputeNailPositionsUseCase()
    val validateArrangement = ValidateArrangementUseCase()
    val snapFrame = SnapFrameUseCase()
    val formatMeasurement = FormatMeasurementUseCase()

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(SettingsPreferences(context))
    }

    val wallRepository: WallRepository by lazy {
        WallRepositoryImpl(
            preferences = WallPreferences(context),
            solveArrangement = solveArrangement,
            computeNails = computeNails,
            defaultEyeHeightCm = settingsRepository.settings.value.eyeHeightCm
        )
    }

    val planRepository: PlanRepository by lazy {
        val settings = settingsRepository.settings.value
        PlanRepositoryImpl(
            defaultGapCm = settings.defaultGapCm,
            defaultEyeHeightCm = settings.eyeHeightCm
        )
    }

    fun init(appContext: Context) {
        if (application == null) {
            application = appContext.applicationContext
        }
    }
}
