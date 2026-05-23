package com.gitje.courtscorewear.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun BadmintonGameScreen(backToStart: () -> Unit) {
    val badmintonViewModel: BadmintonViewModel = koinViewModel()

    val ongoingScoring = remember { badmintonViewModel.ongoingScoring }
    val wonTeam by badmintonViewModel.wonTeam.collectAsState()
    val servingTeam by badmintonViewModel.servingTeam.collectAsState()
    val team1SetHistory by badmintonViewModel.team1SetResults.collectAsState()
    val team2SetHistory by badmintonViewModel.team2SetResults.collectAsState()

    val team1Score = ongoingScoring.count { it == 1 }
    val team2Score = ongoingScoring.count { it == 2 }

    // Scoring animation fields & triggers
    var prevTeam1 by remember { mutableIntStateOf(team1Score) }
    var prevTeam2 by remember { mutableIntStateOf(team2Score) }
    var animationTriggerTeam1 by remember { mutableIntStateOf(0) }
    var animationTriggerTeam2 by remember { mutableIntStateOf(0) }

    LaunchedEffect(ongoingScoring.size) {
        val newT1 = ongoingScoring.count { it == 1 }
        val newT2 = ongoingScoring.count { it == 2 }

        when {
            newT1 > prevTeam1 -> {
                animationTriggerTeam1 += 1
            }

            newT2 > prevTeam2 -> {
                animationTriggerTeam2 += 1
            }
        }

        prevTeam1 = newT1
        prevTeam2 = newT2
    }

    if (wonTeam == 0) {
        if (servingTeam == 0) {
            ServerPickerScreen {
                badmintonViewModel.setServingTeam(it)
            }
        } else {
            Box(Modifier.fillMaxHeight(0.8f)) {
                BadmintonScoringUI(
                    servingTeam,
                    team1Score,
                    team2Score,
                    team1SetHistory,
                    team2SetHistory,
                    teamScored = { badmintonViewModel.teamScored(it) },
                    popTriggerTeam1 = animationTriggerTeam1,
                    popTriggerTeam2 = animationTriggerTeam2,
                )

                CompactButton(
                    {
                        badmintonViewModel.undoLastScore()
                    },
                    Modifier.align(alignment = Alignment.CenterStart),
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_undo), null)
                }
            }
        }
    } else {
        GameFinishedScreen(wonTeam) { backToStart() }
    }
}

@Composable
fun BadmintonScoringUI(
    servingTeam: Int,
    team1Score: Int,
    team2Score: Int,
    team1SetHistory: List<Int>,
    team2SetHistory: List<Int>,
    teamScored: (Int) -> Unit,
    popTriggerTeam1: Int = 0,
    popTriggerTeam2: Int = 0,
) {
    // Used as a center-base for the scoring animations
    var rootCenter by remember { mutableStateOf(Offset.Zero) }

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow()
                rootCenter = Offset(position.x + coordinates.size.width / 2f, position.y + coordinates.size.height / 2f)
            },
    ) {
        Column {
            Column(
                modifier =
                    Modifier
                        .weight(0.7f)
                        .background(Color.Red.copy(0.5f))
                        .fillMaxWidth()
                        .clickable(onClick = {
                            teamScored(1)
                        }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Team 1")
                Row(Modifier.fillMaxWidth(0.7f)) {
                    if (servingTeam == 1) {
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            if (team1Score % 2 == 0) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                                    null,
                                    modifier =
                                        Modifier
                                            .size(20.dp),
                                )
                                AnimatedScoreText(
                                    score = "$team1Score",
                                    popTrigger = popTriggerTeam1,
                                    rootCenter = rootCenter,
                                )
                            }
                        }
                        Text(" | ", Modifier.weight(0.1f))
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (team1Score % 2 != 0) {
                                AnimatedScoreText(
                                    score = "$team1Score",
                                    popTrigger = popTriggerTeam1,
                                    rootCenter = rootCenter,
                                    modifier = Modifier.padding(start = 3.dp),
                                )
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                                    null,
                                    modifier =
                                        Modifier
                                            .size(20.dp),
                                )
                            }
                        }
                    } else {
                        Text(
                            text = if (team2Score % 2 == 0) "$team1Score" else "",
                            modifier = Modifier.weight(0.4f),
                            textAlign = TextAlign.End,
                        )

                        Text(" | ", Modifier.weight(0.1f))

                        Text(
                            text = if (team2Score % 2 != 0) "$team1Score" else "",
                            modifier =
                                Modifier
                                    .weight(0.4f)
                                    .padding(start = 3.dp),
                        )
                    }
                }
            }

            Column(
                modifier =
                    Modifier
                        .weight(0.7f)
                        .fillMaxWidth()
                        .background(Color.Blue.copy(0.5f))
                        .clickable(onClick = {
                            teamScored(2)
                        }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Team 2")
                Row(Modifier.fillMaxWidth(0.7f)) {
                    if (servingTeam == 2) {
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            if (team2Score % 2 != 0) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                                    null,
                                    modifier =
                                        Modifier
                                            .size(20.dp),
                                )
                                AnimatedScoreText(
                                    score = "$team2Score",
                                    popTrigger = popTriggerTeam2,
                                    rootCenter = rootCenter,
                                )
                            }
                        }
                        Text(" | ", Modifier.weight(0.1f))
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (team2Score % 2 == 0) {
                                AnimatedScoreText(
                                    score = "$team2Score",
                                    popTrigger = popTriggerTeam2,
                                    rootCenter = rootCenter,
                                    modifier = Modifier.padding(start = 3.dp),
                                )
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                                    null,
                                    modifier =
                                        Modifier
                                            .size(20.dp),
                                )
                            }
                        }
                    } else {
                        Text(
                            text = if (team1Score % 2 != 0) "$team2Score" else "",
                            modifier = Modifier.weight(0.4f),
                            textAlign = TextAlign.End,
                        )

                        Text(" | ", Modifier.weight(0.1f))

                        Text(
                            text = if (team1Score % 2 == 0) "$team2Score" else "",
                            modifier =
                                Modifier
                                    .weight(0.4f)
                                    .padding(start = 3.dp),
                        )
                    }
                }
            }
        }

        Row(Modifier.align(Alignment.Center)) {
            team1SetHistory.forEachIndexed { index, score ->
                Column(
                    Modifier.padding(horizontal = 3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val score2 = team2SetHistory[index]
                    Text(
                        text = "$score",
                        textDecoration = if (score > score2) TextDecoration.Underline else null,
                        fontWeight = if (score > score2) FontWeight.Bold else null,
                    )
                    Text(
                        text = "$score2",
                        textDecoration = if (score2 > score) TextDecoration.Underline else null,
                        fontWeight = if (score2 > score) FontWeight.Bold else null,
                    )
                }
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun BadmintonScoringUIPreview() {
    CourtScoreTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center,
        ) {
            BadmintonScoringUI(
                1,
                12,
                9,
                listOf(21, 21),
                team2SetHistory = listOf(16, 18),
                teamScored = { },
            )
        }
    }
}
