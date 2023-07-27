package com.example.videoeditor.data.logic

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import com.example.videoeditor.ui.widgets.TabPosition
import kotlin.math.sqrt

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.tabIndicator(
    tabPosition: TabPosition,
    animationSpec: AnimationSpec<Dp>
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = tabPosition
    }

){
    val currentTabWidth by animateDpAsState(
        targetValue = tabPosition.width,
        animationSpec = animationSpec
    )
    val indicatorOffset by animateDpAsState(
        targetValue = tabPosition.left,
        animationSpec = animationSpec
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
        .fillMaxHeight()
}

fun Context.showMessage(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun getDistanceFromEvent(motionEvent: MotionEvent): Int {
    val dx = motionEvent.getX(0) - motionEvent.getX(1)
    val dy = motionEvent.getY(0) - motionEvent.getY(1)
    return sqrt((dx * dx) + (dy * dy)).toInt()
}
