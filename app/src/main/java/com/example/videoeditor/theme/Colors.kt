package com.example.videoeditor.theme


import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val white = Color.White
private val black = Color.Black
private val grey = Color(0xFF6A6A6A)
private val blackTransparent30 = Color(0x4D000000)
private val blackTransparent50 = Color(0x80000000)
private val purple = Color(0xFF7A78FF)
private val strokeGrey = Color(0xFF343437)
private val tabBackground = Color(0xFF303033)
private val greyTabText = Color(0xFF666666)

data class VideoEditorColors(
    val material: ColorScheme,
    val whiteText: Color,
    private val secondaryMaterial: Color,
    private val backgroundMaterial: Color,
    private val white: Color,
    private val black: Color,
    private val grey: Color,
    private val blackTransparent30: Color,
    private val purple: Color,
    private val strokeGrey: Color,
    private val tabBackground: Color,
    private val greyTabText: Color,
    private val blackTransparent50: Color

    ){
    val primary: Color get() = material.primary
    val background: Color get() = backgroundMaterial
    val secondary: Color get() = secondaryMaterial
    val titleColor: Color get() = whiteText
    val whiteColor: Color get() = white
    val blackColor: Color get() = black
    val greyColor: Color get() = grey
    val blackTransparent30Color: Color get() = blackTransparent30
    val blackTransparent50Color: Color get() = blackTransparent50
    val purpleColor: Color get() = purple
    val strokeGreyColor: Color get() = strokeGrey
    val tabBackgroundColor: Color get() = tabBackground
    val greyTabTextColor: Color get() = greyTabText

}

val LightColors = VideoEditorColors(
    material = lightColorScheme(),
    whiteText = white,
    secondaryMaterial = black,
    backgroundMaterial = black,
    white = white,
    black = black,
    grey = grey,
    blackTransparent30 = blackTransparent30,
    purple = purple,
    strokeGrey = strokeGrey,
    tabBackground = tabBackground,
    greyTabText = greyTabText,
    blackTransparent50 = blackTransparent50
)