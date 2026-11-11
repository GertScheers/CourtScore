package com.gitje.courtscorewear.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun BadmintonGameScreen(backToStart: () -> Unit) {
    val badmintonViewModel: BadmintonViewModel = koinViewModel()

    val ongoingScoring = remember { badmintonViewModel.scoreHistory }
    val wonTeam by badmintonViewModel.wonTeam.collectAsState()
    val servingTeam by badmintonViewModel.servingTeam.collectAsState()
    val opponentSetResults by badmintonViewModel.opponentSetResults.collectAsState()
    val mySetResults by badmintonViewModel.mySetResults.collectAsState()

    var opponentAnimationTrigger by remember { mutableIntStateOf(0) }
    var myAnimationTrigger by remember { mutableIntStateOf(0) }

    TimeText()
    if (wonTeam == 0) {
        if (servingTeam == 0) {
            ServerPickerScreen {
                badmintonViewModel.setServingTeam(it)
            }
        } else {
            val opponentScore = ongoingScoring.last().scoreAfter.points.opponentScore
            val myScore = ongoingScoring.last().scoreAfter.points.myScore

            // Scoring animation fields & triggers
            var prevOppScore by remember { mutableIntStateOf(opponentScore) }
            var prevMyScore by remember { mutableIntStateOf(myScore) }

            var watchWidth by remember { mutableStateOf(0.dp) }

            LaunchedEffect(ongoingScoring.size) {
                val newOppScore = ongoingScoring.last().scoreAfter.points.opponentScore
                val newMyScore = ongoingScoring.last().scoreAfter.points.myScore

                when {
                    newOppScore > prevOppScore -> {
                        opponentAnimationTrigger += 1
                    }

                    newMyScore > prevMyScore -> {
                        myAnimationTrigger += 1
                    }
                }

                prevOppScore = newOppScore
                prevMyScore = newMyScore
            }

            Box(modifier = Modifier.fillMaxHeight(0.8f).onGloballyPositioned {
                watchWidth = it.size.width.dp
            }) {
                Box {
                    BadmintonScoringUI(
                        servingTeam,
                        opponentScore,
                        myScore,
                        badmintonViewModel.getOpponentColor(),
                        badmintonViewModel.getMyColor(),
                        opponentSetResults,
                        mySetResults,
                        teamScored = { badmintonViewModel.teamScored(it) },
                        opponentTriggerAnim = opponentAnimationTrigger,
                        myTriggerAnim = myAnimationTrigger,
                    )
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .offset(x = ((-watchWidth/4)-10.dp).value.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .clip(CircleShape)
                            .size(130.dp)
                            .background(MaterialTheme.colors.primary)
                            .padding(end = 15.dp)
                            .clickable(onClick = { badmintonViewModel.undoLastScore() })
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_undo),
                            null,
                            tint = Color.Black,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
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
    opponentScore: Int,
    myScore: Int,
    opponentColor: String,
    myColor: String,
    opponentSetHistory: List<Int>,
    mySetHistory: List<Int>,
    teamScored: (Int) -> Unit,
    opponentTriggerAnim: Int = 0,
    myTriggerAnim: Int = 0,
) {
    // Used as a center-base for the scoring animations
    var rootCenter by remember { mutableStateOf(Offset.Zero) }

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow()
                rootCenter = Offset(
                    position.x + coordinates.size.width / 2f,
                    position.y + coordinates.size.height / 2f
                )
            },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Column(
                modifier =
                    Modifier
                        .weight(0.7f)
                        .background(Color(("#$opponentColor").toColorInt()).copy(0.5f))
                        .fillMaxWidth()
                        .clickable(onClick = {
                            teamScored(1)
                        }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Opponent")
                Row(Modifier.fillMaxWidth(0.7f)) {
                    if (servingTeam == 1) {
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            if (opponentScore % 2 == 0) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                                    null,
                                    modifier =
                                        Modifier
                                            .size(20.dp),
                                )
                                AnimatedScoreText(
                                    score = "$opponentScore",
                                    popTrigger = opponentTriggerAnim,
                                    rootCenter = rootCenter,
                                )
                            }
                        }
                        Text(" | ", Modifier.weight(0.1f))
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (opponentScore % 2 != 0) {
                                AnimatedScoreText(
                                    score = "$opponentScore",
                                    popTrigger = opponentTriggerAnim,
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
                            text = if (myScore % 2 == 0) "$opponentScore" else "",
                            modifier = Modifier.weight(0.4f),
                            textAlign = TextAlign.End,
                        )

                        Text(" | ", Modifier.weight(0.1f))

                        Text(
                            text = if (myScore % 2 != 0) "$opponentScore" else "",
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
                        .background(Color(("#$myColor").toColorInt()).copy(0.5f))
                        .clickable(onClick = {
                            teamScored(2)
                        }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("You")
                Row(Modifier.fillMaxWidth(0.7f)) {
                    if (servingTeam == 2) {
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            if (myScore % 2 != 0) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                                    null,
                                    modifier =
                                        Modifier
                                            .size(20.dp),
                                )
                                AnimatedScoreText(
                                    score = "$myScore",
                                    popTrigger = myTriggerAnim,
                                    rootCenter = rootCenter,
                                )
                            }
                        }
                        Text(" | ", Modifier.weight(0.1f))
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (myScore % 2 == 0) {
                                AnimatedScoreText(
                                    score = "$myScore",
                                    popTrigger = myTriggerAnim,
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
                            text = if (opponentScore % 2 != 0) "$myScore" else "",
                            modifier = Modifier.weight(0.4f),
                            textAlign = TextAlign.End,
                        )

                        Text(" | ", Modifier.weight(0.1f))

                        Text(
                            text = if (opponentScore % 2 == 0) "$myScore" else "",
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
            opponentSetHistory.forEachIndexed { index, score ->
                Column(
                    Modifier.padding(horizontal = 3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val score2 = mySetHistory[index]
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
                Color.Red.toArgb().toHexString(),
                Color.Blue.toArgb().toHexString(),
                listOf(21, 21),
                mySetHistory = listOf(16, 18),
                teamScored = { },
            )
        }
    }
}
