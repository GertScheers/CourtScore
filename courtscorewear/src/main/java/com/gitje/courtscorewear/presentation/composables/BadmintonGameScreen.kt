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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
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
import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun BadmintonGameScreen(backToStart: () -> Unit) {
    val badmintonViewModel: BadmintonViewModel = koinViewModel()

    val scoreHistory by badmintonViewModel.scoreHistory.collectAsState()
    val wonPlayer by badmintonViewModel.wonPlayer.collectAsState()
    val servingTeam by badmintonViewModel.servingTeam.collectAsState()
    val team1SetHistory by badmintonViewModel.team1SetHistory.collectAsState()
    val team2SetHistory by badmintonViewModel.team2SetHistory.collectAsState()

    var team1Score by remember(scoreHistory) { mutableIntStateOf(scoreHistory.count { it == 1 }) }
    var team2Score by remember(scoreHistory) { mutableIntStateOf(scoreHistory.count { it == 2 }) }

    if (wonPlayer == 0) {
        if (servingTeam == 0) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Who will start?")
                Button(
                    onClick = { badmintonViewModel.setServingTeam(1) },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("They/(s)he") }
                Button(
                    onClick = { badmintonViewModel.setServingTeam(2) },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("We/me") }
            }
        } else {
            Box(Modifier.fillMaxHeight(0.8f)) {
                    BadmintonScoringUI(
                        servingTeam,
                        team1Score,
                        team2Score,
                        team1SetHistory,
                        team2SetHistory
                    ) {
                        badmintonViewModel.teamScored(it)
                    }

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                    null,
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .size(32.dp)
                )
                CompactButton(
                    {
                        badmintonViewModel.undoLastScore()
                    },
                    Modifier.align(alignment = Alignment.CenterStart)
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_undo), null)
                }
            }
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Team ${if (wonPlayer == 1) "1" else "2"} wins!", fontSize = 26.sp)
            Text(if (wonPlayer == 1) "Better luck next time!" else "Congratulations!")
            Button(onClick = { backToStart() }) { Text("Return") }
        }
    }
}

@Composable
fun BadmintonScoringUI(
    servingTeam: Int,
    team1Score: Int,
    team2Score: Int,
    team1SetHistory: List<Int>,
    team2SetHistory: List<Int>,
    teamScored: (Int) -> Unit
) {
    Box(
        Modifier.fillMaxWidth()
    ) {
        Column {
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .background(Color.Red.copy(0.5f))
                    .fillMaxWidth()
                    .clickable(onClick = {
                        teamScored(1)
                    }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Team 1")
                Row(Modifier.fillMaxWidth(0.7f)) {
                    if (servingTeam == 1) {
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (team1Score % 2 == 0) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_even),
                                    null
                                )
                                Text("$team1Score")
                            }
                        }
                        Text(" | ", Modifier.weight(0.1f))
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (team1Score % 2 != 0) {
                                Text("$team1Score", Modifier.padding(start = 3.dp))
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_uneven),
                                    null
                                )
                            }
                        }
                    } else {
                        Text(
                            if (team2Score % 2 == 0) "$team1Score" else "",
                            Modifier.weight(0.4f),
                            textAlign = TextAlign.End
                        )
                        Text(" | ", Modifier.weight(0.1f))
                        Text(
                            if (team2Score % 2 != 0) "$team1Score" else "",
                            Modifier
                                .weight(0.4f)
                                .padding(start = 3.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth()
                    .background(Color.Blue.copy(0.5f))
                    .clickable(onClick = {
                        teamScored(2)
                    }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Team 2")
                Row(Modifier.fillMaxWidth(0.7f)) {
                    if (servingTeam == 2) {
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (team2Score % 2 != 0) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_even),
                                    null
                                )
                                Text("$team2Score")
                            }
                        }
                        Text(" | ", Modifier.weight(0.1f))
                        Row(
                            Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (team2Score % 2 == 0) {
                                Text("$team2Score", Modifier.padding(start = 3.dp))
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_uneven),
                                    null
                                )
                            }
                        }
                    } else {
                        Text(
                            if (team1Score % 2 != 0) "$team2Score" else "",
                            Modifier.weight(0.4f),
                            textAlign = TextAlign.End
                        )
                        Text(" | ", Modifier.weight(0.1f))
                        Text(
                            if (team1Score % 2 == 0) "$team2Score" else "",
                            Modifier
                                .weight(0.4f)
                                .padding(start = 3.dp)
                        )
                    }
                }
            }
        }

        Row(Modifier.align(Alignment.Center)) {
            team1SetHistory.forEachIndexed { index, score ->
                Column(Modifier.padding(horizontal = 3.dp)) {
                    val score2 = team2SetHistory[index]
                    Text(
                        text = "$score",
                        textDecoration = if(score > score2) TextDecoration.Underline else null,
                        fontWeight = if(score > score2) FontWeight.Bold else null)
                    Text(
                        text = "$score2",
                        textDecoration = if(score2 > score) TextDecoration.Underline else null,
                        fontWeight = if(score2 > score) FontWeight.Bold else null)
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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            BadmintonScoringUI(
                1,
                12,
                9,
                listOf(21,21),
                team2SetHistory = listOf(16,18)
            ) { }
        }
    }
}