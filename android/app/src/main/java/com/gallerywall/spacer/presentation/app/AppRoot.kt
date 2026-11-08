package com.gallerywall.spacer.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallerywall.spacer.core.di.AppViewModelFactory
import com.gallerywall.spacer.core.navigation.Screen
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.presentation.game.GameScreen
import com.gallerywall.spacer.presentation.gameover.GameOverScreen
import com.gallerywall.spacer.presentation.menu.MenuScreen
import com.gallerywall.spacer.presentation.settings.SettingsScreen
import com.gallerywall.spacer.presentation.setup.SetupScreen
import com.gallerywall.spacer.presentation.splash.SplashScreen

@Composable
fun AppRoot(viewModel: AppViewModel = viewModel(factory = AppViewModelFactory)) {
    val screen by viewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by viewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        viewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlasterBg)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when {
                    initialState is Screen.Splash ->
                        fadeIn(tween(420)) togetherWith fadeOut(tween(300))

                    targetState is Screen.GameOver ->
                        (fadeIn(tween(380)) + scaleIn(initialScale = 0.97f)) togetherWith
                            fadeOut(tween(240))

                    else ->
                        (slideInHorizontally(tween(300)) { it / 6 } + fadeIn(tween(300))) togetherWith
                            (
                                slideOutHorizontally(tween(220)) { -it / 8 } +
                                    fadeOut(tween(220))
                                )
                }
            },
            label = "screen"
        ) { current ->
            when (current) {
                Screen.Splash -> SplashScreen(
                    onFinished = { viewModel.navigate(Screen.Menu) }
                )

                Screen.Menu -> MenuScreen(
                    onOpenSetup = { viewModel.navigate(Screen.Setup) },
                    onOpenGame = { viewModel.navigate(Screen.Game) },
                    onOpenSettings = { viewModel.navigate(Screen.Settings) }
                )

                Screen.Setup -> SetupScreen(
                    onBack = { viewModel.back() },
                    onOpenGame = { viewModel.navigate(Screen.Game) }
                )

                Screen.Game -> GameScreen(
                    onBack = { viewModel.back() },
                    onOpenResult = { planId -> viewModel.navigate(Screen.GameOver(planId)) },
                    onOpenSettings = { viewModel.navigate(Screen.Settings) }
                )

                is Screen.GameOver -> GameOverScreen(
                    planId = current.planId,
                    onBack = { viewModel.back() },
                    onOpenSetup = { viewModel.navigate(Screen.Setup) },
                    onMenu = { viewModel.backToMenu() }
                )

                Screen.Settings -> SettingsScreen(
                    onBack = { viewModel.back() }
                )
            }
        }
    }
}
