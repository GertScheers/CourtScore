package com.gitje.courtscorewear.logic

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.collections.isNotEmpty

class BadmintonViewModel(
    application: Application,
    val sharedPreferences: SharedPreferences
) : BaseViewModel(application) {
    override fun teamScored(player: Int) {
        ongoingScoring.add(player)

        val setOver = checkIfSetIsWon()
        //setOver == null -> Continue game
        setOver?.let {
            if (it == 1) {
                _servingTeam.value = 1
            } else {
                _servingTeam.value = 2
            }
            _team1SetResults.value.add(ongoingScoring.count { score -> score == 1 })
            _team2SetResults.value.add(ongoingScoring.count { score -> score == 2 })
            ongoingScoring.clear()
            _wonTeam.value = checkIfGameIsWon()
        } ?: run {
            _servingTeam.value = ongoingScoring.last()
        }
    }

    override fun checkIfSetIsWon(): Int? {
        val team1Score = ongoingScoring.count { it == 1 }
        val team2Score = ongoingScoring.count { it == 2 }

        if (team1Score > 20 && team1Score - team2Score > 1)
            return 1
        if (team2Score > 20 && team2Score - team1Score > 1)
            return 2

        return null
    }

    override fun undoLastScore() {
        if (ongoingScoring.isNotEmpty())
            ongoingScoring.removeAt(ongoingScoring.size - 1)
        else if (_team1SetResults.value.isNotEmpty()) {
            //Undo won set, fill history with setHistory's values and continue playing 'closed set'
            var pointsForTeam1 = _team1SetResults.value.last()
            var pointsForTeam2 = _team2SetResults.value.last()

            //If someone won the set with '21', set them back to 20
            if (pointsForTeam1 > pointsForTeam2)
                pointsForTeam1--
            else
                pointsForTeam2--

            repeat(pointsForTeam1) {
                ongoingScoring.add(1)
            }
            repeat(pointsForTeam2) {
                ongoingScoring.add(2)
            }
            _team1SetResults.value.removeAt(_team1SetResults.value.lastIndex)
            _team2SetResults.value.removeAt(_team2SetResults.value.lastIndex)
        }
    }

    fun getTeam1Color(): String {
        return sharedPreferences.getString(SETTING_TEAM_1_COLOR, Color.Red.toArgb().toHexString()) ?: Color.Red.toArgb().toHexString()
    }

    fun getTeam2Color(): String {
        return sharedPreferences.getString(SETTING_TEAM_2_COLOR, Color.Blue.toArgb().toHexString()) ?: Color.Blue.toArgb().toHexString()
    }
}