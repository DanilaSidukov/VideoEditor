package com.example.videoeditor.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.videoeditor.R
import com.example.videoeditor.data.MediaPreviewEntity
import com.example.videoeditor.theme.VideoEditorTheme
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class MediaItem(
    val mediaList: List<MediaPreviewEntity>?,
)

@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChooseMediaScreen(
    //onItemClicked: (MediaPreviewEntity) -> Unit
) {

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val tabRowItem = listOf(
        MediaItem(
            listOf(
                MediaPreviewEntity(
                    R.drawable.mock_image,
                    //ConverterImage("mock_image.png"),
                    "12:11"
                ),
                MediaPreviewEntity(
                    R.drawable.mock_image,
                    //ConverterImage("mock_image.png"),
                    "7:12"
                ),
                MediaPreviewEntity(
                    R.drawable.mock_image,
                    //ConverterImage("mock_image.png"),
                    "1:54"
                ),
                MediaPreviewEntity(
                    R.drawable.mock_image,
                    //ConverterImage("mock_image.png"),
                    "52:01"
                )
            ),
        ),
        MediaItem(
            null
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
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier
                        .background(color = VideoEditorTheme.colors.background)
                        .width(208.dp)
                        .height(35.dp)

                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, color = VideoEditorTheme.colors.strokeGreyColor)
                        .align(Alignment.CenterHorizontally),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            color = Color.Transparent
                        )
                    },
                    divider = { Divider() },
                    edgePadding = 0.dp
                ) {
                    tabRowItem.forEachIndexed { index, mediaItem ->
                        val tabName = if (index == 0) "Storage"
                        else "Cloud"
                        val isSelected = pagerState.currentPage == index
                        val backgroundTab =
                            if (isSelected) VideoEditorTheme.colors.tabBackgroundColor
                            else VideoEditorTheme.colors.background
                        Tab(
                            selected = pagerState.currentPage == index,
                            modifier = Modifier
                                .height(35.dp)
                                .width(104.dp)
                                .background(
                                    backgroundTab,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = tabName,
                                    style = VideoEditorTheme.typography.interFamilyRegular12,
                                    textAlign = TextAlign.Center,
                                    color = if (isSelected) VideoEditorTheme.colors.whiteText
                                    else VideoEditorTheme.colors.greyTabTextColor
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    count = tabRowItem.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 50.dp),
                    verticalAlignment = Top
                ) {
                    MediaPreviewItem(
                        list = tabRowItem[pagerState.currentPage].mediaList,
                        //onItemClicked = onItemClicked
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPreviewItem(
    list: List<MediaPreviewEntity>?,
    //onItemClicked: (MediaPreviewEntity) -> Unit
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration.provides(null)
    ) {
        if (!list.isNullOrEmpty()) {
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
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.5.dp))
                        //.background(color = VideoEditorTheme.colors.purpleColor)
                        .padding(horizontal = 48.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 42.dp),
                    onClick = { }
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

@Composable
fun ItemMediaPreview(
    item: MediaPreviewEntity = MediaPreviewEntity(
        R.drawable.mock_image,
        //ConverterImage("mock_image.png"),
        "12:11"
    ),
    //onItemClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(114.dp)
            .width(114.dp)
            .clip(RoundedCornerShape(8.dp))

    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = R.drawable.mock_image,
                //                Image(
//                    bitmap = item.itemImage.asImageBitmap(),
//                    contentDescription = null
//                )
            ),
            null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = item.itemDuration!!,
            style = VideoEditorTheme.typography.interFamilyRegular10,
            color = VideoEditorTheme.colors.whiteText,
            modifier = Modifier
                .align(
                    Alignment.BottomStart
                )
                .padding(start = 10.dp, bottom = 6.dp)
        )
    }
}

