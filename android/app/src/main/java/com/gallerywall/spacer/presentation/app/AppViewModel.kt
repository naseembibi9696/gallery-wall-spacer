package com.gallerywall.spacer.presentation.app

import androidx.lifecycle.ViewModel
import com.gallerywall.spacer.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = currentScreen.asStateFlow()

    private val backAvailable = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = backAvailable.asStateFlow()

    fun navigate(target: Screen) {
        val current = currentScreen.value
        if (current != Screen.Splash && current != target) {
            backStack.add(current)
        }
        currentScreen.value = target
        backAvailable.value = backStack.isNotEmpty()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        currentScreen.value = previous
        backAvailable.value = backStack.isNotEmpty()
    }

    fun backToMenu() {
        backStack.clear()
        currentScreen.value = Screen.Menu
        backAvailable.value = false
    }
}
