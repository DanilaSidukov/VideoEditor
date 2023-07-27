package com.example.videoeditor.ui.screens.choosemedia

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.videoeditor.R
import com.example.videoeditor.data.logic.convertFirstFrameToBitmap
import com.example.videoeditor.data.logic.convertImageToBitmap
import com.example.videoeditor.data.logic.showMessage
import com.example.videoeditor.data.service.MediaFiles
import com.example.videoeditor.theme.VideoEditorTheme
import com.example.videoeditor.ui.widgets.CircleProgressBar
import com.example.videoeditor.ui.widgets.TabView
import com.example.videoeditor.utility.Screen
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaItem(
    val mediaItemsList: List<MediaFiles>,
)

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "MutableCollectionMutableState"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMediaScreen(
    chooseMediaViewModel: ChooseMediaViewModel,
    navController: NavHostController,
    onItemClicked: (MediaFiles) -> Unit,
) {

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    var tabPosition by remember { mutableStateOf(0) }
    var isPageScrolled by remember { mutableStateOf(false) }
    var pagePosition by remember { mutableStateOf(0) }

    var isDataLoaded by remember { mutableStateOf(false) }

    val mediaList = remember { mutableListOf<MediaFiles>() }

    LaunchedEffect(true) {
        chooseMediaViewModel.mediaFlow.collect {
            mediaList.add(it)
            isDataLoaded = true
        }
    }

    val tabRowItem = listOf(
        MediaItem(
            mediaList.toList()
        ),
        MediaItem(
            emptyList()
        )
    )

    Scaffold(
        containerColor = VideoEditorTheme.colors.background,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = "Media",
                        style = VideoEditorTheme.typography.interFamilyBold16
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = VideoEditorTheme.colors.background,
                    titleContentColor = VideoEditorTheme.colors.whiteColor
                ),
                navigationIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        tint = VideoEditorTheme.colors.whiteColor,
                        modifier = Modifier
                            .padding(start = 31.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())

            ) {
                if (isDataLoaded) {
                    TabView(pagePosition) { newPosition ->
                        isPageScrolled = true
                        tabPosition = newPosition
                    }

                    HorizontalPager(
                        count = tabRowItem.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 30.dp),
                        verticalAlignment = Top
                    ) {
                        pagePosition = pagerState.currentPage
                        if (isPageScrolled) {
                            coroutineScope.launch {
                                pagerState.scrollToPage(tabPosition)
                                isPageScrolled = false
                            }
                        }
                        MediaPreviewItem(
                            list = tabRowItem[pagerState.currentPage].mediaItemsList,
                            onItemClicked = onItemClicked,
                            pagePosition,
                            navController
                        )
                    }
                } else Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircleProgressBar()
                }

            }
        }
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreviewItem(
    list: List<MediaFiles>,
    onItemClicked: (MediaFiles) -> Unit,
    tabPosition: Int,
    navController: NavHostController,
) {

    val chosenItemList: MutableList<MediaFiles> by mutableStateOf(mutableListOf())
    var isMediaChosen by remember { mutableStateOf(true) }

    CompositionLocalProvider(
        LocalOverscrollConfiguration.provides(null)
    ) {
        if (tabPosition == 0) {
            Box {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(list.size) { index ->
                        ItemMediaPreview(
                            item = list[index],
                            onItemClicked = {
                            },
                            chosenItemList
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .background(color = VideoEditorTheme.colors.background)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.5.dp))
                        .padding(horizontal = 48.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 42.dp),
                    onClick = {
                        isMediaChosen = if (chosenItemList.isNotEmpty()) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "media",
                                value = chosenItemList
                            )
                            navController.navigate(Screen.Edit.route)
                            true
                        } else {
                            false
                        }
                    }
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Create",
                        color = VideoEditorTheme.colors.whiteText,
                        style = VideoEditorTheme.typography.interFamilyMedium14,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
            if (!isMediaChosen) {
                LocalContext.current.showMessage("No media selected, please choose something")
                isMediaChosen = true
            }
        } else {
            Text(
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center,
                text = "Coming soon",
                style = VideoEditorTheme.typography.interFamilyMedium14,
                color = VideoEditorTheme.colors.whiteColor
            )
        }
    }
}


@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun ItemMediaPreview(
    item: MediaFiles,
    onItemClicked: (MediaFiles) -> Unit,
    chosenItemList: MutableList<MediaFiles>,
) {

    println("drawing again")

    val context = LocalContext.current

    var tempBool by remember { mutableStateOf(false) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(item.mediaId) {
        launch(Dispatchers.Default) {
            imageBitmap = if (item.isVideo) convertFirstFrameToBitmap(context, item.mediaId)
            else convertImageToBitmap(context, item.mediaPath!!)
        }
    }

    Box(
        modifier = Modifier
            .height(114.dp)
            .width(114.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                tempBool = !tempBool
            },
    ) {
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        if (tempBool) {
            Box(
                modifier = Modifier
                    .padding(end = 7.dp, top = 6.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(VideoEditorTheme.colors.purpleColor)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_choose),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = VideoEditorTheme.colors.whiteColor
                )
            }
            chosenItemList.add(item)
        } else chosenItemList.remove(item)

        if (item.mediaDuration != null) {
            Text(
                text = item.mediaDuration.toString(),
                style = VideoEditorTheme.typography.interFamilyRegular10,
                color = VideoEditorTheme.colors.whiteText,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 6.dp)
                    .background(
                        color = VideoEditorTheme.colors.blackTransparent50Color,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(vertical = 2.dp, horizontal = 5.dp)
            )
        }
    }

}


