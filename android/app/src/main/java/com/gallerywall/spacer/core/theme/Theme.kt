package com.gallerywall.spacer.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GalleryColorScheme = lightColorScheme(
    primary = InkPrimary,
    onPrimary = PlasterBg,
    primaryContainer = InkMid,
    onPrimaryContainer = PlasterBg,
    secondary = SurfaceMuted,
    onSecondary = InkDeep,
    tertiary = CoralAccent,
    onTertiary = SurfaceCard,
    background = PlasterBg,
    onBackground = InkDeep,
    surface = SurfaceCard,
    onSurface = InkDeep,
    surfaceVariant = PlasterBgDeepest,
    onSurfaceVariant = InkPrimary,
    outline = SurfaceMuted,
    outlineVariant = SurfaceMuted,
    error = CoralAccent,
    onError = SurfaceCard
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GalleryColorScheme,
        typography = AppTypography,
        content = content
    )
}
