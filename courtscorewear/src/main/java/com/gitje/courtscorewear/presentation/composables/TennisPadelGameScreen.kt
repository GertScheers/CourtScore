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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.logic.TennisPadelViewModel
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import com.gitje.courtscorewear.util.getTennisScore
import org.koin.androidx.compose.koinViewModel
import com.gitje.courtscore.sharedclasses.GameType

@Composable
fun TennisPadelGameScreen(gameType: GameType, backToStart: () -> Unit) {
    val viewModel: TennisPadelViewModel = koinViewModel()

    val wonTeam by viewModel.wonTeam.collectAsState()
    val servingTeam by viewModel.servingTeam.collectAsState()
    val ongoingScore = remember { viewModel.scoreHistory }
    val opponentSetResults by viewModel.opponentSetResults.collectAsState()
    val mySetResults by viewModel.mySetResults.collectAsState()

    var opponentScore by remember(ongoingScore.size) { mutableIntStateOf(ongoingScore.last().scoreAfter.gamePoints.first) }
    var myScore by remember(ongoingScore.size) { mutableIntStateOf(ongoingScore.last().scoreAfter.gamePoints.second) }

    var opponentSetScore by remember(ongoingScore.size) { mutableIntStateOf(ongoingScore.last().scoreAfter.points.opponentScore) }
    var mySetScore by remember(ongoingScore.size) { mutableIntStateOf(ongoingScore.last().scoreAfter.points.myScore) }

    var opponentAnimationTrigger by remember { mutableIntStateOf(0) }
    var myAnimationTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(opponentScore) {
        opponentAnimationTrigger++
    }
    LaunchedEffect(myScore) {
        myAnimationTrigger++
    }

    val vector by remember(gameType) {
        mutableIntStateOf(
            when (gameType) {
                GameType.Tennis -> R.drawable.ic_tennis
                else -> R.drawable.ic_padel
            }
        )
    }

    TimeText()

    if (wonTeam == 0) {
        if (servingTeam == 0) {
            ServerPickerScreen {
                viewModel.setServingTeam(it)
            }
        } else {
            var watchWidth by remember { mutableStateOf(0.dp) }

            Box(modifier = Modifier
                .fillMaxHeight(0.8f)
                .onGloballyPositioned {
                    watchWidth = it.size.width.dp
                }) {
                Box {
                    //Tennis-Padel UI
                    TennisPadelScoringUI(
                        servingPlayer = servingTeam,
                        gameIcon = vector,
                        opponentCurrentPointScore = opponentScore,
                        myCurrentPointScore = myScore,
                        opponentCurrentSetScore = opponentSetScore,
                        myCurrentSetScore = mySetScore,
                        opponentSetHistory = opponentSetResults,
                        mySetHistory = mySetResults,
                        opponentTriggerAnim = opponentAnimationTrigger,
                        myTriggerAnim = myAnimationTrigger,
                        opponentColor = viewModel.getOpponentColor(),
                        myColor = viewModel.getMyColor()
                    ) {
                        viewModel.teamScored(it)
                    }
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .offset(x = ((-watchWidth / 4) - 10.dp).value.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .clip(CircleShape)
                            .size(130.dp)
                            .background(MaterialTheme.colors.primary)
                            .padding(end = 15.dp)
                            .clickable(onClick = { viewModel.undoLastScore() })
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
fun TennisPadelScoringUI(
    servingPlayer: Int,
    gameIcon: Int,
    opponentCurrentPointScore: Int,
    myCurrentPointScore: Int,
    opponentCurrentSetScore: Int,
    myCurrentSetScore: Int,
    opponentSetHistory: List<Int>,
    mySetHistory: List<Int>,
    opponentTriggerAnim: Int,
    myTriggerAnim: Int,
    opponentColor: String,
    myColor: String,
    teamScored: (Int) -> Unit
) {
    var rootCenter by remember { mutableStateOf(Offset.Zero) }

    Column(
        Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow()
                rootCenter = Offset(
                    position.x + coordinates.size.width / 2f,
                    position.y + coordinates.size.height / 2f
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.7f)
                .background(Color(("#$opponentColor").toColorInt()).copy(0.5f))
                .fillMaxWidth()
                .clickable(onClick = {
                    //Score for top team
                    teamScored(1)
                }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text("Opponent")
                        if (servingPlayer == 1) {
                            Icon(
                                imageVector = ImageVector.vectorResource(gameIcon),
                                null,
                                modifier = Modifier
                                    .size(18.dp)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(0.7f), horizontalArrangement = Arrangement.Center) {
                        opponentSetHistory.forEachIndexed { index, score ->
                            val score2 = mySetHistory[index]
                            Text(
                                "$score",
                                textDecoration = if (score > score2) TextDecoration.Underline else null,
                                fontWeight = if (score > score2) FontWeight.Bold else null
                            )
                            Text(" | ")
                        }
                        Text("$opponentCurrentSetScore")
                    }
                }

                AnimatedScoreText(
                    opponentCurrentPointScore.getTennisScore(),
                    opponentTriggerAnim,
                    rootCenter,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
                .background(Color(("#$myColor").toColorInt()).copy(0.5f))
                .clickable(onClick = {
                    //Score for down team
                    teamScored(2)
                }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text("You")
                        if (servingPlayer == 2) {
                            Icon(
                                imageVector = ImageVector.vectorResource(gameIcon),
                                null,
                                modifier = Modifier
                                    .size(18.dp)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(0.7f), horizontalArrangement = Arrangement.Center) {
                        mySetHistory.forEachIndexed { index, score ->
                            val score1 = opponentSetHistory[index]
                            Text(
                                "$score",
                                textDecoration = if (score > score1) TextDecoration.Underline else null,
                                fontWeight = if (score > score1) FontWeight.Bold else null
                            )
                            Text(" | ")
                        }
                        Text("$myCurrentSetScore")
                    }
                }
                AnimatedScoreText(
                    score = myCurrentPointScore.getTennisScore(),
                    popTrigger = myTriggerAnim,
                    rootCenter = rootCenter,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TennisPadelPadelScoringUIPreview() {
    CourtScoreTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TennisPadelScoringUI(
                1,
                gameIcon = R.drawable.ic_padel,
                2,
                2,
                5,
                3,
                listOf(3, 2),
                listOf(6, 6),
                opponentTriggerAnim = 0,
                myTriggerAnim = 0,
                Color.Red.toArgb().toHexString(),
                Color.Blue.toArgb().toHexString()
            ) { }
        }
    }
}