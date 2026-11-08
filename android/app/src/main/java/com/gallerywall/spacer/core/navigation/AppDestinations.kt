package com.gallerywall.spacer.core.navigation

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object Setup : Screen

    data object Game : Screen

    data class GameOver(val planId: String) : Screen

    data object Settings : Screen
}
