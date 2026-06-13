package com.gitje.courtscorewear.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.SplitToggleChip
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.SplitSwitchButton
import androidx.wear.compose.material3.Text

@Composable
fun SettingsScreen(
    keepScreenOn: Boolean,
    changeKeepScreenOn: (Boolean) -> Unit,
    changeTeam1Color: (String) -> Unit,
    changeTeam2Color: (String) -> Unit
) {
    var showColorSelector by remember { mutableStateOf(Pair(false, 0)) }

    ScalingLazyColumn(anchorType = ScalingLazyListAnchorType.ItemStart) {
        item {
            SplitSwitchButton(
                keepScreenOn,
                onCheckedChange = { changeKeepScreenOn(it) },
                toggleContentDescription = "Keep screen on",
                onContainerClick = { changeKeepScreenOn(!keepScreenOn) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Keep screen on")
            }
        }

        item {
            SplitToggleChip(
                true,
                { },
                label = { Text("Team 1 Color") },
                contentPadding = PaddingValues(start = 14.dp),
                onClick = { showColorSelector = Pair(true, 1) },
                toggleControl = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SplitToggleChip(
                true,
                { showColorSelector = Pair(true, 2) },
                label = { Text("Team 2 Color") },
                onClick = { },
                contentPadding = PaddingValues(start = 14.dp),
                toggleControl = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Blue)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showColorSelector.first) {
        ColorSelector {
            if (showColorSelector.second == 1) {
                changeTeam1Color(it)
            } else if (showColorSelector.second == 2) {
                changeTeam2Color(it)
            }
        }
    }
}

@Composable
fun ColorSelector(colorSelected: (String) -> Unit) {
    // Partition items into rows of columnsPerRow
    val colors = remember {
        listOf(
            Color.Blue,
            Color.Red,
            Color.Green,
            Color.Black,
            Color.LightGray,
            Color.Magenta,
            Color.Cyan,
            Color.Yellow,
            Color.White,
            Color.DarkGray,
            Color.Gray
        )
    }
    // Partition into rows alternating 1,2,1,2...
    val rows = remember(colors) {
        val r = mutableListOf<List<Color?>>()
        var i = 0
        var wantSingle = true
        while (i < colors.size) {
            if (wantSingle) {
                r.add(listOf(colors[i]))
                i += 1
            } else {
                if (i + 1 < colors.size) {
                    r.add(listOf(colors[i], colors[i + 1]))
                    i += 2
                } else {
                    // not enough left for a pair -> add a spacer
                    r.add(listOf(colors[i], null))
                    i += 1
                }
            }
            wantSingle = !wantSingle
        }
        r
    }

    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .offset(y = (-20).dp),
        verticalArrangement = Arrangement.spacedBy(-(20.dp))
    ) {
        rows.forEach { rowItems ->
            item {
                val isSingle = rowItems.size == 1

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (isSingle) Arrangement.Center else Arrangement.SpaceBetween
                ) {
                    if (isSingle) {
                        // center single item
                        Box(
                            Modifier
                                .size(50.dp)
                                .background(
                                    rowItems[0]!!,
                                    CircleShape
                                )//Singles should never be empty
                                .border(BorderStroke(3.dp, Color.White), CircleShape)
                                .clickable(onClick = {
                                    colorSelected(
                                        rowItems[0]!!.toArgb().toHexString()
                                    )
                                })
                        )
                    } else {
                        Box(
                            Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    rowItems[0]!!,
                                    CircleShape
                                )//First one should never be empty
                                .border(BorderStroke(3.dp, Color.White), CircleShape)
                                .clickable(onClick = {
                                    colorSelected(
                                        rowItems[0]!!.toArgb().toHexString()
                                    )
                                })
                        )
                        rowItems[1]?.let { rowItem ->
                            Box(
                                Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(rowItem, CircleShape)
                                    .border(BorderStroke(3.dp, Color.White), CircleShape)
                                    .clickable(onClick = {
                                        colorSelected(
                                            rowItem.toArgb().toHexString()
                                        )
                                    })
                            )
                        } ?: run {
                            Spacer(Modifier.size(50.dp))
                        }
                    }
                }
            }
        }
    }
}