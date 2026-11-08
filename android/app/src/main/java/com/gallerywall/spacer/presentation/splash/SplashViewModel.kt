package com.gallerywall.spacer.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallerywall.spacer.core.config.GameConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val finishedState = MutableStateFlow(false)
    val finished: StateFlow<Boolean> = finishedState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(GameConfig.LOADER_DURATION_MS)
            finishedState.value = true
        }
    }
}
