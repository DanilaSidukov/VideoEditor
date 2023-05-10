package com.example.videoeditor.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.videoeditor.R
import androidx.compose.ui.text.TextStyle

val MedulaOneFamily = FontFamily(
    Font(R.font.medulaone_regular)
)

val InterFamily = FontFamily(
    Font(R.font.inter_bold),
    Font(R.font.inter_medium),
    Font(R.font.inter_regular),
    Font(R.font.inter_light)
)

internal fun MedulaOneMainTitleStyle(weight: FontWeight, size: TextUnit = 16.sp) =
    TextStyle(
        fontFamily = MedulaOneFamily,
        fontWeight = weight,
        fontSize = size,
        )

internal fun InterFamilyStyle(weight: FontWeight, size: TextUnit = 16.sp) =
    TextStyle(
        fontFamily = InterFamily,
        fontWeight = weight,
        fontSize = size,
    )

data class VideoEditorTypography(
    val material: Typography = Typography(),
    val medulaOneRegularTitle: TextStyle = MedulaOneMainTitleStyle(FontWeight.Normal, 34.sp),
    val interFamilyBold: TextStyle = InterFamilyStyle(FontWeight.Bold, 30.sp),
    val interFamilyMedium18 : TextStyle = InterFamilyStyle(FontWeight.Medium, 18.sp),
    val interFamilyMedium14 : TextStyle = InterFamilyStyle(FontWeight.Medium, 14.sp),
    val interFamilyMedium12 : TextStyle = InterFamilyStyle(FontWeight.Medium, 12.sp),
    val interFamilyRegular14: TextStyle = InterFamilyStyle(FontWeight.Normal, 14.sp),
    val interFamilyLight: TextStyle = InterFamilyStyle(FontWeight.Light, 14.sp),
    val interFamilyBold16: TextStyle = InterFamilyStyle(FontWeight.Bold, 16.sp),
    val interFamilyRegular12: TextStyle = InterFamilyStyle(FontWeight.Normal, 12.sp),
    val interFamilyRegular10: TextStyle = InterFamilyStyle(FontWeight.Normal, 10.sp),
)