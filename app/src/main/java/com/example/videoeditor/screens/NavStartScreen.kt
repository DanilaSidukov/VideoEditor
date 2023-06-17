package com.example.videoeditor.screens

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.videoeditor.di.viewModelWithFactory
import com.example.videoeditor.screens.choosemedia.ChooseMediaScreen
import com.example.videoeditor.screens.main.MainScreen
import com.example.videoeditor.theme.VideoEditorTheme
import com.example.videoeditor.utility.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppStartScreen(
    navController: NavHostController,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VideoEditorTheme.colors.background)
    ) {
        NavigationGraph(navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavigationGraph(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.ChooseMedia.route) {
        composable(Screen.GetStoragePermission.route) {
            GetMediaPermissions(
                onPermissionGranted = { MainScreen(navController) },
                onPermissionDenied = { OnPermissionDenied() }
            )
        }
        composable(Screen.ChooseMedia.route) {
            ChooseMediaScreen(
            chooseMediaViewModel = viewModelWithFactory(),
            navController,
            onItemClicked = {}
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetMediaPermissions(
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable () -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES
        ) else listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    when {
        permissionState.allPermissionsGranted -> onPermissionGranted()
        permissionState.shouldShowRationale -> onPermissionDenied()
    }

}

@Composable
fun OnPermissionDenied() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VideoEditorTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Permission denied, please, go to settings and enable permission",
            color = VideoEditorTheme.colors.whiteText,
            textAlign = TextAlign.Center,
            style = VideoEditorTheme.typography.interFamilyRegular14
        )
    }
}