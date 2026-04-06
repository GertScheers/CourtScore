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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.logic.TennisPadelViewModel
import com.gitje.courtscorewear.models.GameType
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import com.gitje.courtscorewear.util.getTennisScore
import org.koin.androidx.compose.koinViewModel

@Composable
fun TennisPadelGameScreen(backToStart: () -> Unit) {
    val viewModel: TennisPadelViewModel = koinViewModel()

    val gameType by viewModel.gameType.collectAsState()
    val wonTeam by viewModel.wonTeam.collectAsState()
    val servingTeam by viewModel.servingTeam.collectAsState()
    val ongoingScore by viewModel.ongoingScoring.collectAsState()
    val ongoingSetResults by viewModel.ongoingSetResults.collectAsState()
    val team1SetHistory by viewModel.team1SetResults.collectAsState()
    val team2SetHistory by viewModel.team2SetResults.collectAsState()

    var team1Score by remember(ongoingScore) { mutableIntStateOf(ongoingScore.count { it == 1 }) }
    var team2Score by remember(ongoingScore) { mutableIntStateOf(ongoingScore.count { it == 2 }) }

    var team1SetScore by remember(ongoingSetResults) { mutableIntStateOf(ongoingSetResults.count { it == 1 }) }
    var team2SetScore by remember(ongoingSetResults) { mutableIntStateOf(ongoingSetResults.count { it == 2 }) }

    val vector by remember(gameType) {
        mutableIntStateOf(
            when (gameType) {
                GameType.Tennis -> R.drawable.ic_tennis
                else -> R.drawable.ic_padel
            }
        )
    }

    if (wonTeam == 0) {
        if (servingTeam == 0) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Who will start?")
                Button(
                    onClick = { viewModel.setServingTeam(1) },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("They/(s)he") }
                Button(
                    onClick = { viewModel.setServingTeam(2) },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("We/me") }
            }
        } else {
            Box(Modifier.fillMaxHeight(0.8f)) {
                //Tennis-Padel UI
                TennisPadelScoringUI(
                    servingTeam,
                    vector,
                    team1Score,
                    team2Score,
                    team1SetScore,
                    team2SetScore,
                    team1SetHistory,
                    team2SetHistory
                ) {
                    viewModel.teamScored(it)
                }

                CompactButton(
                    {
                        viewModel.undoLastScore()
                    },
                    Modifier.align(alignment = Alignment.CenterStart)
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_undo), null)
                }
            }
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Team ${if (wonTeam == 1) "1" else "2"} wins!", fontSize = 26.sp)
            Text(if (wonTeam == 1) "Better luck next time!" else "Congratulations!")
            Button(onClick = { backToStart() }) { Text("Return") }
        }
    }
}

@Composable
fun TennisPadelScoringUI(
    servingPlayer: Int,
    gameIcon: Int,
    team1CurrentPointScore: Int,
    team2CurrentPointScore: Int,
    team1CurrentSetScore: Int,
    team2CurrentSetScore: Int,
    team1SetHistory: List<Int>,
    team2SetHistory: List<Int>,
    teamScored: (Int) -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(0.7f)
                .background(Color.Red.copy(0.5f))
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
                        Text("Team 1")
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
                        team1SetHistory.forEachIndexed { index, score ->
                            val score2 = team2SetHistory[index]
                            Text(
                                "$score",
                                textDecoration = if (score > score2) TextDecoration.Underline else null,
                                fontWeight = if (score > score2) FontWeight.Bold else null
                            )
                            Text(" | ")
                        }
                        Text("$team1CurrentSetScore")
                    }
                }

                Text(
                    team1CurrentPointScore.getTennisScore(),
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
                .background(Color.Blue.copy(0.5f))
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
                        Text("Team 2")
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
                        team2SetHistory.forEachIndexed { index, score ->
                            val score1 = team1SetHistory[index]
                            Text(
                                "$score",
                                textDecoration = if (score > score1) TextDecoration.Underline else null,
                                fontWeight = if (score > score1) FontWeight.Bold else null
                            )
                            Text(" | ")
                        }
                        Text("$team2CurrentSetScore")
                    }
                }
                Text(
                    team2CurrentPointScore.getTennisScore(),
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
                listOf(6, 6)
            ) { }
        }
    }
}