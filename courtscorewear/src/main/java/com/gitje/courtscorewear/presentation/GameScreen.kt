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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun GameScreen(gameType: GameType, backToStart: () -> Unit) {
    fun handleScoreForSports(
        score1: Int,
        score2: Int
    ): Int? {//Return player Id if a set is finished
        when (gameType) {
            GameType.Badminton -> {
                if (score1 > 20 && score1 - score2 > 1)
                    return 1
                if (score2 > 20 && score2 - score1 > 1)
                    return 2
            }

            GameType.Tennis, GameType.Padel -> {
                if ((score1 == 3 && score2 < 3)
                    || score1 == 5
                )
                    return 1
                if ((score2 == 3 && score1 < 3)
                    || score2 == 5
                )
                    return 2
            }
        }
        return null
    }

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
    val setHistory = remember { mutableStateListOf<Int>() }
    var team1Score by remember { mutableIntStateOf(0) }
    var team1sets by remember { mutableIntStateOf(0) }
    var team2Score by remember { mutableIntStateOf(0) }
    var team2sets by remember { mutableIntStateOf(0) }
    var activePlayer by remember { mutableIntStateOf(0) }
    var wonPlayer by remember { mutableIntStateOf(0) }

    LaunchedEffect(scoreHistory.size) {
        team1Score = scoreHistory.count { it == 1 }
        team2Score = scoreHistory.count { it == 2 }
        activePlayer = scoreHistory.lastOrNull() ?: activePlayer
        when (handleScoreForSports(team1Score, team2Score)) {
            1 -> {
                setHistory.add(1)
                scoreHistory.clear()
            }

            2 -> {
                setHistory.add(2)
                scoreHistory.clear()
            }

            else -> {}//Do nothing, set continues
        }
        team1sets = setHistory.count { it == 1 }
        team2sets = setHistory.count { it == 2 }
        if (team1sets == 2) {
            //Team 1 Wins
            wonPlayer = 1
        } else if (team2sets == 2) {
            //Team2 Wins
            wonPlayer = 2
        }
    }

    if (wonPlayer == 0) {
        if (activePlayer == 0) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Who will start?")
                Button(
                    onClick = { activePlayer = 1 },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("They/(s)he") }
                Button(
                    onClick = { activePlayer = 2 },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("We/me") }
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
                        Text("Team 1 ($team1sets)")
                        Row(Modifier.fillMaxWidth(0.7f)) {
                            if (activePlayer == 1) {
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
                                //Score for down team
                                scoreHistory.add(2)
                            }),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Team 2 ($team2sets)")
                        Row(Modifier.fillMaxWidth(0.7f)) {
                            if (activePlayer == 2) {
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
                Icon(
                    imageVector = ImageVector.vectorResource(vector),
                    null,
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .size(32.dp)
                )
                CompactButton(
                    {
                        if (scoreHistory.isNotEmpty())
                            scoreHistory.removeAt(scoreHistory.size - 1)
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
            Button(onClick = { backToStart() } ) { Text("Return") }
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
            GameScreen(GameType.Tennis) { }
        }
    }
}

enum class GameType {
    Tennis,
    Padel,
    Badminton
}