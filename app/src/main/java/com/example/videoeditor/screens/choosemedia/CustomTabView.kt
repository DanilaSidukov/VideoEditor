package com.example.videoeditor.screens.choosemedia

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.videoeditor.data.logic.tabIndicator
import com.example.videoeditor.theme.VideoEditorTheme


enum class SubComposeID {
    PRE_CALCULATE_ITEM,
    ITEM,
    INDICATOR
}

data class TabPosition(
    val left: Dp, val width: Dp,
)

@Composable
fun TabTitle(
    title: String,
    position: Int,
    onClick: (Int) -> Unit,
    isSelected: Boolean
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 25.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(position)
            },
        style = VideoEditorTheme.typography.interFamilyRegular12,
        textAlign = TextAlign.Center,
        color = if (isSelected) VideoEditorTheme.colors.whiteText
        else VideoEditorTheme.colors.greyTabTextColor
    )
}


@Composable
fun TabView(
    position: Int,
    onPositionChanged: (Int) -> Unit,
) {
    val items = listOf("Storage", "Cloud")

    CustomTabRow(
        selectedTabPosition = position,
        tabItem = {
            items.forEachIndexed { index, s ->
                val isSelected = position == index
                TabTitle(title = s, position = index, onClick = {
                    onPositionChanged(index)
                }, isSelected )
            }
        }
    )

}

@Composable
fun CustomTabRow(
    containerColor: Color = VideoEditorTheme.colors.background,
    indicatorColor: Color = VideoEditorTheme.colors.tabBackgroundColor,
    containerShape: Shape = RectangleShape,
    indicatorShape: Shape = RoundedCornerShape(8.dp),
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300, easing = FastOutSlowInEasing),
    fixedSize: Boolean = true,
    selectedTabPosition: Int = 0,
    tabItem: @Composable () -> Unit,
) {

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            color = containerColor,
            shape = containerShape,
            modifier = Modifier
                .padding(horizontal = 100.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            SubcomposeLayout(
                modifier = Modifier
                    .selectableGroup()
                    .border(BorderStroke(1.dp, color = VideoEditorTheme.colors.strokeGreyColor)),
            ) { constraints ->
                val tabMeasurable: List<Placeable> =
                    subcompose(SubComposeID.PRE_CALCULATE_ITEM, tabItem)
                        .map { it.measure(constraints) }

                val itemCount = tabMeasurable.size
                val maxItemWidth = tabMeasurable.maxOf { it.width }
                val maxItemHeight = tabMeasurable.maxOf { it.height }

                val tabPlaceables = subcompose(SubComposeID.ITEM, tabItem).map {
                    val modifyConstraints = if (fixedSize) constraints.copy(
                        minWidth = maxItemWidth,
                        maxWidth = maxItemWidth,
                        minHeight = maxItemHeight
                    ) else constraints
                    it.measure(modifyConstraints)
                }

                val tabPosition = tabPlaceables.mapIndexed { index, placeable ->
                    val itemWidth = if (fixedSize) maxItemWidth
                    else placeable.width
                    val x = if (fixedSize) {
                        maxItemWidth * index
                    } else {
                        val leftTabWidth = tabPlaceables.take(index).sumOf { it.width }
                        leftTabWidth
                    }
                    TabPosition(x.toDp(), itemWidth.toDp())
                }

                val tabRowWidth = if (fixedSize) maxItemWidth * itemCount
                else tabPlaceables.sumOf { it.width }

                layout(tabRowWidth, maxItemHeight) {
                    subcompose(SubComposeID.INDICATOR) {
                        Box(
                            modifier = Modifier
                                .tabIndicator(tabPosition[selectedTabPosition], animationSpec)
                                .fillMaxWidth()
                                .height(maxItemHeight.toDp())
                                .background(color = indicatorColor, shape = indicatorShape),
                        )
                    }.forEach {
                        it.measure(Constraints.fixed(tabRowWidth, maxItemHeight))
                            .placeRelative(0, 0)
                    }

                    tabPlaceables.forEachIndexed { index, placeable ->
                        val x = if (fixedSize) {
                            maxItemWidth * index
                        } else {
                            val leftTabWidth = tabPlaceables.take(index).sumOf { it.width }
                            leftTabWidth
                        }
                        placeable.placeRelative(x, 0)
                    }
                }
            }
        }
    }
}