package com.example.videoeditor.screens.editmedia

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.videoeditor.data.Settings.Companion.MAX_STRETCHING
import com.example.videoeditor.data.Settings.Companion.MEASURE_STEP
import com.example.videoeditor.data.Settings.Companion.MIN_STRETCHING
import com.example.videoeditor.data.logic.getDistanceFromEvent
import com.example.videoeditor.theme.VideoEditorTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun MeasureRow(){

    val secondsArray = listOf(0..35)

    var distanceZoomIn by remember { mutableStateOf(0) }
    var distanceZoomOut by remember { mutableStateOf(0) }

    var spaceBetweenMeasure by remember { mutableStateOf(15.dp) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .background(Color.Black)
            .padding(start = 20.dp, end = 20.dp)
            .pointerInteropFilter {
                if (it.pointerCount == 2){
                    val pure = it.action and MotionEvent.ACTION_MASK

                    if (pure == MotionEvent.ACTION_POINTER_DOWN){
                        distanceZoomIn = getDistanceFromEvent(it)
                        distanceZoomOut = getDistanceFromEvent(it)
                    } else {
                        val currentDistance = getDistanceFromEvent(it)
                        if (currentDistance > distanceZoomIn){
                            var finalSize = spaceBetweenMeasure + MEASURE_STEP
                            if (finalSize > MAX_STRETCHING) finalSize = MAX_STRETCHING
                            spaceBetweenMeasure = finalSize
                        } else {
                            if (currentDistance < distanceZoomOut){
                            var finalSize = spaceBetweenMeasure - MEASURE_STEP
                                if (finalSize < MIN_STRETCHING) finalSize = MIN_STRETCHING
                            spaceBetweenMeasure = finalSize
                            }
                        }
                    }
                }
                return@pointerInteropFilter true
            },
        horizontalArrangement = Arrangement.spacedBy(spaceBetweenMeasure),
        state = rememberLazyListState(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(secondsArray.last().last + 1){
            if (it % 5 == 0){
                Text(
                    text = "${it}s",
                    style = VideoEditorTheme.typography.interFamilyMedium12,
                    color = VideoEditorTheme.colors.whiteColor,
                )
            } else {
                Canvas(
                    modifier = Modifier.height(7.dp)
                ){
                    drawLine(
                        start = Offset(x = this.size.width, y = 0f),
                        end = Offset(x = 0f, y = this.size.height),
                        strokeWidth = 5f,
                        color = Color(0xFF6A6A6A),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }

}