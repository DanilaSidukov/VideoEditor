package com.example.videoeditor.theme


import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val white = Color.White
private val black = Color.Black
private val grey = Color(0xFF6A6A6A)
private val blackTransparent = Color(0x4D000000)
private val purple = Color(0xFF7A78FF)

data class VideoEditorColors(
    val material: ColorScheme,
    val whiteText: Color,
    private val secondaryMaterial: Color,
    private val backgroundMaterial: Color,
    private val white: Color,
    private val black: Color,
    private val grey: Color,
    private val blackTransparent: Color,
    private val purple: Color


){
    val primary: Color get() = material.primary
    val background: Color get() = backgroundMaterial
    val secondary: Color get() = secondaryMaterial
    val titleColor: Color get() = whiteText
    val whiteColor: Color get() = white
    val blackColor: Color get() = black
    val greyColor: Color get() = grey
    val blackTransparentColor: Color get() = blackTransparent
    val purpleColor: Color get() = purple

}

val LightColors = VideoEditorColors(
    material = lightColorScheme(),
    whiteText = white,
    secondaryMaterial = black,
    backgroundMaterial = black,
    white = white,
    black = black,
    grey = grey,
    blackTransparent = blackTransparent,
    purple = purple

)