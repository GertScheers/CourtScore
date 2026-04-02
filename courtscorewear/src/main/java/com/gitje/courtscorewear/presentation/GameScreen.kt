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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
fun GameScreen(gameType: GameType, gameSets: Int, backToStart: () -> Unit) {
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
                if ((score1 == 4 && score2 < 3)
                    || score1 == 5
                )
                    return 1
                if ((score2 == 4 && score1 < 3)
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
    val setOrPointHistory = remember { mutableStateListOf<Int>() }
    var team1Score by remember { mutableIntStateOf(0) }
    var team1BadmintonSet by remember { mutableIntStateOf(0) }
    val team1TennisSetHistory = remember { mutableStateListOf<Int>() }
    var team2Score by remember { mutableIntStateOf(0) }
    var team2BadmintonSet by remember { mutableIntStateOf(0) }
    val team2TennisSetHistory = remember { mutableStateListOf<Int>() }
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
            } else if (team2BadmintonSet == (gameSets/2)) {
                //Team2 Wins
                wonPlayer = 2
            }
        } else {
            //Tennis or Padel
            var team1WonSets = 0
            var team2WonSets = 0

            team1TennisSetHistory.forEachIndexed { index, i ->
                val team2SetScore = team2TennisSetHistory[index]
                if((i == 6 && team2SetScore < 5)
                    || i == 7) {
                    team1WonSets++
                } else if ((i < 5 && team2SetScore == 6)
                    || team2SetScore == 7) {
                    team2WonSets++
                }
            }

            if(team1WonSets > (gameSets / 2)) {
                //Team 1 wins
                wonPlayer = 1
            } else if(team2WonSets > (gameSets/2)){
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
            Button(onClick = { backToStart() }) { Text("Return") }
        }
    }
}

@Composable
fun BadmintonScoringUI(
    servingTeam: Int,
    team1Score: Int,
    team2Score: Int,
    team1Sets: Int,
    team2Sets: Int,
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
            Text("Team 1 ($team1Sets)")
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
                    //Score for down team
                    teamScored(2)
                }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Team 2 ($team2Sets)")
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
}

@Composable
fun TennisScoringUI(
    servingPlayer: Int,
    team1Score: Int,
    team2Score: Int,
    team1Sets: Int,
    team2Sets: Int,
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
                                imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_even),
                                null,
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(0.7f), horizontalArrangement = Arrangement.Center) {
                        team1SetHistory.forEach { Text("$it | ") }
                        Text("$team1Sets")
                    }
                }

                Text(
                    team1Score.getTennisScore(),
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
                                imageVector = ImageVector.vectorResource(R.drawable.ic_active_player_even),
                                null,
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(0.7f), horizontalArrangement = Arrangement.Center) {
                        team2SetHistory.forEach { Text("$it | ") }
                        Text("$team2Sets")
                    }
                }
                Text(
                    team2Score.getTennisScore(),
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
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
            GameScreen(GameType.Tennis, 1) { }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun BadmintonScorePreview() {
    CourtScoreTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            BadmintonScoringUI(1, 15, 12, 1, 1) {

            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TennisScorePreview() {
    CourtScoreTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            TennisScoringUI(1, 2, 1, 1, 1, listOf(4, 2), listOf(6, 6)) {

            }
        }
    }
}

enum class GameType {
    None,
    Tennis,
    Padel,
    Badminton
}