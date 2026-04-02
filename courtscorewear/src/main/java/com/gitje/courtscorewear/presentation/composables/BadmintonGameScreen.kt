package com.gitje.courtscorewear.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.presentation.BadmintonScoringUI
import com.gitje.courtscorewear.presentation.GameType
import com.gitje.courtscorewear.presentation.TennisScoringUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun BadmintonGameScreen(sets: Int, backToStart: () -> Unit) {
    val badmintonViewModel: BadmintonViewModel = koinViewModel()

    fun handleScoreForSports(
        score1: Int,
        score2: Int
    ): Int? {
        if (score1 > 20 && score1 - score2 > 1)
            return 1
        if (score2 > 20 && score2 - score1 > 1)
            return 2

        return null
    }

    val scoreHistory = remember { mutableStateListOf<Int>() }
    val setOrPointHistory = remember { mutableStateListOf<Int>() }
    var team1Score by remember { mutableIntStateOf(0) }
    var team1BadmintonSet by remember { mutableIntStateOf(0) }
    var team2Score by remember { mutableIntStateOf(0) }
    var team2BadmintonSet by remember { mutableIntStateOf(0) }
    var servingTeam by remember { mutableIntStateOf(0) }
    var wonPlayer by remember { mutableIntStateOf(0) }

    LaunchedEffect(scoreHistory.size) {
        team1Score = scoreHistory.count { it == 1 }
        team2Score = scoreHistory.count { it == 2 }
        if (gameType == GameType.Badminton)//Team that makes point takes serve next
            servingTeam = scoreHistory.lastOrNull() ?: servingTeam

        when (handleScoreForSports(team1Score, team2Score)) {
            1 -> {
                setOrPointHistory.add(1)
                scoreHistory.clear()
                if (gameType == GameType.Tennis || gameType == GameType.Padel)//Serving player switches at end of point
                {
                    val playerOnePoints = setOrPointHistory.count { it == 1 }
                    val playerTwoPoints = setOrPointHistory.count { it == 2 }
                    //Check if set is done or continue
                    if ((playerOnePoints == 6 && playerTwoPoints < 5) ||
                        playerOnePoints == 7
                    ) {
                        team1TennisSetHistory.add(playerOnePoints)
                        team2TennisSetHistory.add(playerTwoPoints)
                        setOrPointHistory.clear()
                    }

                    servingTeam = if (servingTeam == 1) 2 else 1
                }
            }

            2 -> {
                setOrPointHistory.add(2)
                scoreHistory.clear()
                //Check if set is done or continue
                if (gameType == GameType.Tennis || gameType == GameType.Padel)//Serving player switches at end of point
                {
                    val playerOnePoints = setOrPointHistory.count { it == 1 }
                    val playerTwoPoints = setOrPointHistory.count { it == 2 }
                    //Check if set is done or continue
                    if (((playerTwoPoints == 6 && playerOnePoints < 5) ||
                                playerTwoPoints == 7)
                    ) {
                        team1TennisSetHistory.add(playerOnePoints)
                        team2TennisSetHistory.add(playerTwoPoints)
                        setOrPointHistory.clear()
                    }
                    servingTeam = if (servingTeam == 1) 2 else 1
                }
            }

            else -> {}//Do nothing, set continues
        }
        team1BadmintonSet = setOrPointHistory.count { it == 1 }
        team2BadmintonSet = setOrPointHistory.count { it == 2 }
        if (gameType == GameType.Badminton) {
            if (team1BadmintonSet > (gameSets / 2)) {
                //Team 1 Wins
                wonPlayer = 1
            } else if (team2BadmintonSet == (gameSets / 2)) {
                //Team2 Wins
                wonPlayer = 2
            }
        } else {
            //Tennis or Padel
            var team1WonSets = 0
            var team2WonSets = 0

            team1TennisSetHistory.forEachIndexed { index, i ->
                val team2SetScore = team2TennisSetHistory[index]
                if ((i == 6 && team2SetScore < 5)
                    || i == 7
                ) {
                    team1WonSets++
                } else if ((i < 5 && team2SetScore == 6)
                    || team2SetScore == 7
                ) {
                    team2WonSets++
                }
            }

            if (team1WonSets > (gameSets / 2)) {
                //Team 1 wins
                wonPlayer = 1
            } else if (team2WonSets > (gameSets / 2)) {
                //Team 2 wins
                wonPlayer = 2
            }
        }
    }

    if (wonPlayer == 0) {
        if (servingTeam == 0) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Who will start?")
                Button(
                    onClick = { servingTeam = 1 },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("They/(s)he") }
                Button(
                    onClick = { servingTeam = 2 },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("We/me") }
            }
        } else {
            Box(Modifier.fillMaxHeight(0.8f)) {
                if (gameType == GameType.Badminton) {
                    BadmintonScoringUI(
                        servingTeam,
                        team1Score,
                        team2Score,
                        team1BadmintonSet,
                        team2BadmintonSet
                    ) {
                        scoreHistory.add(it)
                    }
                } else {
                    //Tennis-Padel UI
                    TennisScoringUI(
                        servingTeam,
                        team1Score,
                        team2Score,
                        setOrPointHistory.count { it == 1 },
                        setOrPointHistory.count { it == 2 },
                        team1TennisSetHistory,
                        team2TennisSetHistory
                    ) {
                        //Cancel ADV for other team if they had, else add point
                        if ((it == 1 && scoreHistory.count { s -> s == 2 } == 4) ||
                            (it == 2 && scoreHistory.count { s -> s == 1 } == 4))
                            scoreHistory.removeAt(scoreHistory.lastIndex)
                        else
                            scoreHistory.add(it)
                    }
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
            Button(onClick = { backToStart() }) { Text("Return") }
        }
    }
}