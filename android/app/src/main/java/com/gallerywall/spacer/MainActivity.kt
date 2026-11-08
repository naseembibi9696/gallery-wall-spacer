package com.gallerywall.spacer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gallerywall.spacer.core.di.ServiceLocator
import com.gallerywall.spacer.core.theme.AppTheme
import com.gallerywall.spacer.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
