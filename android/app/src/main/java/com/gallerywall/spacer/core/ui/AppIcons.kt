package com.gallerywall.spacer.core.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

private fun materialIcon(name: String, pathData: String): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).addPath(
        pathData = addPathNodes(pathData),
        fill = SolidColor(Color.Black)
    ).build()

object AppIcons {

    val Add: ImageVector by lazy {
        materialIcon("Add", "M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z")
    }

    val Check: ImageVector by lazy {
        materialIcon("Check", "M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z")
    }

    val Place: ImageVector by lazy {
        materialIcon(
            "Place",
            "M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7z" +
                "m0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"
        )
    }

    val Refresh: ImageVector by lazy {
        materialIcon(
            "Refresh",
            "M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-8 8s3.58 8 8 8c3.73 0 6.84-2.55 " +
                "7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 " +
                "4.22 1.78L13 11h7V4l-2.35 2.35z"
        )
    }

    val PlayArrow: ImageVector by lazy {
        materialIcon("PlayArrow", "M8 5v14l11-7z")
    }

    val Settings: ImageVector by lazy {
        materialIcon(
            "Settings",
            "M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61" +
                "l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54" +
                "c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94" +
                "l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58" +
                "c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32" +
                "c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84" +
                "c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22" +
                "l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58z" +
                "M12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z"
        )
    }
}
