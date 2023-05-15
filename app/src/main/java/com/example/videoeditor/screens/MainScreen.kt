package com.example.videoeditor.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.room.util.getColumnIndexOrThrow
import coil.compose.rememberAsyncImagePainter
import com.example.videoeditor.R
import com.example.videoeditor.data.ProjectItem
import com.example.videoeditor.data.Settings.Companion.mockProjects
import com.example.videoeditor.data.logic.showMessage
import com.example.videoeditor.di.viewModelWithFactory
import com.example.videoeditor.screens.choosemedia.ChooseMediaScreen
import com.example.videoeditor.theme.VideoEditorTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController
) {

    var isIconClicked by remember { mutableStateOf(false) }
    var isPermissionGranted by remember { mutableStateOf(false) }

    GetMediaPermissions(
        onPermissionGranted = { isPermissionGranted = true },
        onPermissionDenied = {
            isPermissionGranted = false
            OnPermissionDenied()
        }
    )

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = {
                        Text(
                            text = "WESHOT",
                            style = VideoEditorTheme.typography.medulaOneRegularTitle,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = VideoEditorTheme.colors.background,
                        titleContentColor = VideoEditorTheme.colors.whiteText
                    ),
                    actions = {
                        IconButton(onClick = { Unit }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_lightning),
                                null,
                                tint = VideoEditorTheme.colors.whiteColor,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                        IconButton(onClick = { Unit }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                                contentDescription = null,
                                tint = VideoEditorTheme.colors.whiteColor
                            )
                        }
                    },
                )
            }, content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = VideoEditorTheme.colors.background)
                            .padding(top = it.calculateTopPadding())
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            items(5) {
                                ItemPreview(
                                    item = mockProjects
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 48.dp)
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 42.dp)
                                .height(87.dp)
                                .clip(RoundedCornerShape(43.5.dp))
                                .background(color = VideoEditorTheme.colors.blackTransparent30Color),
                            contentAlignment = Alignment.Center
                        ){
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {  }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_camera),
                                        contentDescription = null,
                                        tint = VideoEditorTheme.colors.whiteColor
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(VideoEditorTheme.colors.purpleColor),
                                    contentAlignment = Alignment.Center
                                ){
                                    IconButton(onClick = {
                                        isIconClicked = true
                                    }) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                                            contentDescription = null,
                                            tint = VideoEditorTheme.colors.whiteColor
                                        )
                                    }
                                }
                                IconButton(onClick = {  }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_galery),
                                        contentDescription = null,
                                        tint = VideoEditorTheme.colors.whiteColor
                                    )
                                }
                            }
                        }
                    }
                }
        )

    if (isIconClicked) {
        isIconClicked = false
        if (isPermissionGranted) {
            ChooseMediaScreen(
                chooseMediaViewModel = viewModelWithFactory(),
                onItemClicked = {

                }
            )
        } else {
            GetMediaPermissions(onPermissionGranted = { isPermissionGranted = true }) {
                isPermissionGranted = false
                OnPermissionDenied()
                LocalContext.current.showMessage("Can't open without permission")
            }
        }
    }
}

@Composable
fun ItemPreview(
    item: ProjectItem = mockProjects,
) {

    var showDialog by remember { mutableStateOf(false) }
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, VideoEditorTheme.colors.blackColor),
        startY = sizeImage.height.toFloat() / 4,
        endY = sizeImage.height.toFloat()
    )

    Column(
        modifier = Modifier
            .height(480.dp)
            .fillMaxWidth()
            .paint(
                painter = rememberAsyncImagePainter(model = R.drawable.mock_image),
                contentScale = ContentScale.FillBounds,
            )
            .clip(RoundedCornerShape(15.dp))
            .onGloballyPositioned {
                sizeImage = it.size
            }
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() - 45.dp }
            },
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)

        ) {
            Column() {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        modifier = Modifier
                            .pointerInput(true){
                                detectTapGestures (
                                    onLongPress = {
                                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                                    },
                                )
                            },
                        onClick = {
                            showDialog = true
                        },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_preview_settings),
                            contentDescription = null,
                            tint = VideoEditorTheme.colors.whiteColor
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = item.name!!,
                    style = VideoEditorTheme.typography.interFamilyBold,
                    textAlign = TextAlign.End,
                    color = VideoEditorTheme.colors.whiteColor,
                    modifier = Modifier.padding(start = 30.dp)
                )
                Text(
                    text = item.date!!,
                    style = VideoEditorTheme.typography.interFamilyLight,
                    textAlign = TextAlign.End,
                    color = VideoEditorTheme.colors.whiteColor,
                    modifier = Modifier.padding(start = 30.dp)
                )

                DropdownMenu(
                    expanded = showDialog,
                    modifier = Modifier
                        .background(VideoEditorTheme.colors.whiteColor),
                    onDismissRequest = { showDialog = false},
                    offset = pressOffset.copy(
                        y = pressOffset.y - itemHeight,
                        x = pressOffset.x + 254.dp
                    )
                ) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(96.dp)
                            .height(96.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(VideoEditorTheme.colors.whiteColor),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                    },

                                ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_dialog_edit),
                                    null
                                )
                                Text(
                                    text = "Edit",
                                    style = VideoEditorTheme.typography.interFamilyMedium12,
                                    color = VideoEditorTheme.colors.greyColor,
                                    modifier = Modifier.width(39.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                    }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_dialog_share),
                                    null
                                )
                                Text(
                                    text = "Share",
                                    style = VideoEditorTheme.typography.interFamilyMedium12,
                                    color = VideoEditorTheme.colors.greyColor,
                                    modifier = Modifier.width(39.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                    }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_dialog_delete),
                                    null
                                )
                                Text(
                                    text = "Delete",
                                    style = VideoEditorTheme.typography.interFamilyMedium12,
                                    color = VideoEditorTheme.colors.greyColor,
                                    modifier = Modifier.width(39.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetMediaPermissions(
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable () -> Unit
){
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

            onDispose{
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
fun OnPermissionDenied(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VideoEditorTheme.colors.background),
    ) {
        Text(
            text = "Permission denied, please, go te settings and enable permission",
            color = VideoEditorTheme.colors.whiteText,
            textAlign = TextAlign.Center,
            style = VideoEditorTheme.typography.interFamilyRegular14
        )
    }
}