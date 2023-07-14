package com.example.videoeditor.screens.editmedia

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.videoeditor.R
import com.example.videoeditor.theme.VideoEditorTheme
import com.example.videomanager.VideoManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun EditScreen(
    //navController: NavHostController
){

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = null,
                    tint = VideoEditorTheme.colors.whiteColor,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Edit",
                    style = VideoEditorTheme.typography.interFamilyMedium18,
                    color = VideoEditorTheme.colors.whiteColor
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    tint = VideoEditorTheme.colors.whiteColor,
                    modifier = Modifier
                        .background(
                            color = VideoEditorTheme.colors.blackTransparent50Color,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(7.dp)
                )
            }
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(VideoEditorTheme.colors.background)
        ) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
            ) {
                AndroidView(
                    factory = { context ->
                        android.widget.VideoView(context).apply {
                            
                        }
                    }
                )
//                Image(
//                    painter = painterResource(id = R.drawable.mock_image),
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Fit
//                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null,
                    tint = VideoEditorTheme.colors.whiteColor
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    tint = VideoEditorTheme.colors.whiteColor,
                    modifier = Modifier.size(28.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_full_screen),
                    contentDescription = null,
                    tint = VideoEditorTheme.colors.whiteColor
                )

            }

            MeasureRow()

        }
    }



}