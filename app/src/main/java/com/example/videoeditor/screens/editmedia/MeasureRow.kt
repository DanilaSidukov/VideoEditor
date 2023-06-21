package com.example.videoeditor.screens.editmedia

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.videoeditor.theme.VideoEditorTheme

@Composable
@Preview
fun MeasureRow(){

    val secondsArray = listOf(0..35)

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .background(Color.Black)
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
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