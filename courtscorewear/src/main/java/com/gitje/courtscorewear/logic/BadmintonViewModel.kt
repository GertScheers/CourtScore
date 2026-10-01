package com.gitje.courtscorewear.logic

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.gitje.courtscore.sharedclasses.ScoreEvent
import com.gitje.courtscore.sharedclasses.ScoreSnapshot
import kotlin.collections.isNotEmpty

class BadmintonViewModel(
    application: Application,
    val sharedPreferences: SharedPreferences
) : BaseViewModel(application) {
    override fun teamScored(player: Int) {
        var opponentScore = 0
        var ownScore = 0
        // Keep at 0 if a new set has started, otherwise check last snapshot
        if (scoreHistory.last().scoreAfter.set == ongoingSet) {
            opponentScore = scoreHistory.last().scoreAfter.points.first
            ownScore = scoreHistory.last().scoreAfter.points.second
        }

        if(player == 0) {
            opponentScore++
        } else ownScore++

        scoreHistory.add(
            ScoreEvent(
                player,
                ScoreSnapshot(ongoingSet, Pair(opponentScore, ownScore))
            )
        )

        val setOver = checkIfSetIsWon()
        //setOver == null -> Continue game
        setOver?.let {
            if (it == 1) {
                _servingTeam.value = 1
            } else {
                _servingTeam.value = 2
            }
            _team1SetResults.value.add(scoreHistory.filter { it.scoreAfter.set == ongoingSet }.maxOf { score -> score.scoreAfter.points.first })
            _team1SetResults.value.add(scoreHistory.filter { it.scoreAfter.set == ongoingSet }.maxOf { score -> score.scoreAfter.points.second })
            _wonTeam.value = checkIfGameIsWon()
        } ?: run {
            _servingTeam.value = scoreHistory.last().scoringPlayer
        }
    }

    override fun checkIfSetIsWon(): Int? {
        val team1Score = scoreHistory.last().scoreAfter.points.first
        val team2Score = scoreHistory.last().scoreAfter.points.second

        if (team1Score > 20 && team1Score - team2Score > 1)
            return 1
        if (team2Score > 20 && team2Score - team1Score > 1)
            return 2

        return null
    }

    override fun undoLastScore() {
        scoreHistory.removeAt(scoreHistory.lastIndex)
    }

    fun getTeam1Color(): String {
        return sharedPreferences.getString(SETTING_TEAM_1_COLOR, Color.Red.toArgb().toHexString()) ?: Color.Red.toArgb().toHexString()
    }

    fun getTeam2Color(): String {
        return sharedPreferences.getString(SETTING_TEAM_2_COLOR, Color.Blue.toArgb().toHexString()) ?: Color.Blue.toArgb().toHexString()
    }
}