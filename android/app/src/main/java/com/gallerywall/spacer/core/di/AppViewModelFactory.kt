package com.gallerywall.spacer.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gallerywall.spacer.presentation.app.AppViewModel
import com.gallerywall.spacer.presentation.game.GameViewModel
import com.gallerywall.spacer.presentation.gameover.GameOverViewModel
import com.gallerywall.spacer.presentation.menu.MenuViewModel
import com.gallerywall.spacer.presentation.settings.SettingsViewModel
import com.gallerywall.spacer.presentation.setup.SetupViewModel
import com.gallerywall.spacer.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                wallRepository = ServiceLocator.wallRepository,
                planRepository = ServiceLocator.planRepository,
                settingsRepository = ServiceLocator.settingsRepository
            )

            modelClass.isAssignableFrom(SetupViewModel::class.java) -> SetupViewModel(
                planRepository = ServiceLocator.planRepository,
                solveArrangement = ServiceLocator.solveArrangement,
                computeNails = ServiceLocator.computeNails,
                validateArrangement = ServiceLocator.validateArrangement
            )

            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                planRepository = ServiceLocator.planRepository,
                wallRepository = ServiceLocator.wallRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                solveArrangement = ServiceLocator.solveArrangement,
                computeNails = ServiceLocator.computeNails,
                validateArrangement = ServiceLocator.validateArrangement,
                snapFrame = ServiceLocator.snapFrame
            )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                wallRepository = ServiceLocator.wallRepository,
                planRepository = ServiceLocator.planRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                formatMeasurement = ServiceLocator.formatMeasurement
            )

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                settingsRepository = ServiceLocator.settingsRepository,
                solveArrangement = ServiceLocator.solveArrangement,
                formatMeasurement = ServiceLocator.formatMeasurement
            )

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return viewModel as T
    }
}
