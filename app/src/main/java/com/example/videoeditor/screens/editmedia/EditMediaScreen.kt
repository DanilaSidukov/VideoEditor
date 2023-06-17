package com.example.videoeditor.screens.editmedia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.videoeditor.theme.VideoEditorTheme

@Composable
@Preview
fun EditMediaScreen(
    //navController: NavHostController
){

    Column(modifier = Modifier
        .fillMaxSize()
        .background(VideoEditorTheme.colors.background)
    ) {

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(VideoEditorTheme.colors.whiteColor)

        ) {

        }

    }

}