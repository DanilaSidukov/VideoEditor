package com.example.videoeditor.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

object VideoEditorTheme{
    val colors: VideoEditorColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: VideoEditorTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

internal val LocalColors = staticCompositionLocalOf { LightColors }
internal val LocalTypography = staticCompositionLocalOf { VideoEditorTypography() }