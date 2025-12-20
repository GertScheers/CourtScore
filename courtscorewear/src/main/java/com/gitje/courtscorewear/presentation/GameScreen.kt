package com.gitje.courtscorewear.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import com.gitje.courtscorewear.R

@Composable
fun GameScreen(gameType: GameType) {
    val vector by remember {
        mutableIntStateOf(
            when (gameType) {
                GameType.Tennis -> R.drawable.ic_tennis
                GameType.Padel -> R.drawable.ic_padel
                else -> R.drawable.ic_badminton
            }
        )
    }
    val scoreHistory = remember { mutableStateListOf<Int>() }
    var team1Score by remember { mutableIntStateOf(0) }
    var team2Score by remember { mutableIntStateOf(0) }
    var activePlayer by remember { mutableIntStateOf(0) }

    //TODO: Figure out why this launchedEffect doesn't update on adding an element to the list
    LaunchedEffect(scoreHistory.size) {
        team1Score = scoreHistory.count { it == 1 }
        team2Score = scoreHistory.count { it == 2 }
        activePlayer = scoreHistory.lastOrNull() ?: 0
    }
    
    if(activePlayer == 0) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Who will start?")
            Button(onClick = { activePlayer = 1 }, modifier = Modifier.fillMaxWidth(0.6f)) { Text("They/she/him") }
            Button(onClick = { activePlayer = 2 }, modifier = Modifier.fillMaxWidth(0.6f)) { Text("We/me") }
        }
    } else {
        Box(Modifier.fillMaxHeight(0.8f)) {
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
                            scoreHistory.add(1)
                        }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Team 1")
                    Row(Modifier.fillMaxWidth(0.7f)) {
                        if (activePlayer == 1) {
                            Row(Modifier.weight(0.4f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                                if (team1Score % 2 == 0) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_even),
                                        null
                                    )
                                    Text("$team1Score")
                                }
                            }
                            Text(" | ", Modifier.weight(0.1f))
                            Row(Modifier.weight(0.4f), verticalAlignment = Alignment.CenterVertically) {
                                if (team1Score % 2 != 0) {
                                    Text("$team1Score", Modifier.padding(start = 3.dp))
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_uneven),
                                        null
                                    )
                                }
                            }
                        } else {
                            Text(if (team2Score % 2 == 0) "$team1Score" else "", Modifier.weight(0.4f), textAlign = TextAlign.End)
                            Text(" | ", Modifier.weight(0.1f))
                            Text(if (team2Score % 2 != 0) "$team1Score" else "", Modifier.weight(0.4f).padding(start = 3.dp))
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxWidth()
                        .background(Color.Blue.copy(0.5f))
                        .clickable(onClick = {
                            //Score for down team
                            scoreHistory.add(2)
                        }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Team 2")
                    Row(Modifier.fillMaxWidth(0.7f)) {
                        if (activePlayer == 2) {
                            Row(Modifier.weight(0.4f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                                if (team2Score % 2 != 0) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_even),
                                        null
                                    )
                                    Text("$team2Score")
                                }
                            }
                            Text(" | ", Modifier.weight(0.1f))
                            Row(Modifier.weight(0.4f), verticalAlignment = Alignment.CenterVertically) {
                                if (team2Score % 2 == 0) {
                                    Text("$team2Score", Modifier.padding(start = 3.dp))
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_uneven),
                                        null
                                    )
                                }
                            }
                        } else {
                            Text(if (team1Score % 2 != 0) "$team2Score" else "", Modifier.weight(0.4f), textAlign = TextAlign.End)
                            Text(" | ", Modifier.weight(0.1f))
                            Text(if (team1Score % 2 == 0) "$team2Score" else "", Modifier.weight(0.4f).padding(start = 3.dp))
                        }
                    }
                }
            }
            Icon(
                imageVector = ImageVector.vectorResource(vector),
                null,
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .size(32.dp)
            )
            CompactButton({ }, Modifier.align(alignment = Alignment.CenterStart)) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_undo), null)
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    CourtScoreTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            GameScreen(GameType.Tennis)
        }
    }
}

enum class GameType {
    Tennis,
    Padel,
    Badminton
}