package com.gitje.courtscorewear.logic

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.gitje.courtscore.sharedclasses.PointScore
import com.gitje.courtscore.sharedclasses.ScoreEvent
import com.gitje.courtscore.sharedclasses.ScoreSnapshot

class BadmintonViewModel(
    application: Application,
    val sharedPreferences: SharedPreferences
) : BaseViewModel(application) {
    override fun teamScored(player: Int) {
        var opponentScore = 0
        var ownScore = 0
        // Keep at 0 if a new set has started, otherwise check last snapshot
        if (scoreHistory.last().scoreAfter.set == ongoingSet) {
            opponentScore = scoreHistory.last().scoreAfter.points.opponentScore
            ownScore = scoreHistory.last().scoreAfter.points.myScore
        }

        if(player == 1)
            opponentScore++
        else ownScore++

        scoreHistory.add(
            ScoreEvent(
                player,
                ScoreSnapshot(ongoingSet, PointScore(opponentScore, ownScore))
            )
        )

        val setOver = checkIfSetIsWon()
        //setOver == null -> Continue game
        setOver?.let { setWinner ->
            if (setWinner == 1) {
                _servingTeam.value = 1
            } else {
                _servingTeam.value = 2
            }
            _opponentSetResults.value.add(scoreHistory.filter { it.scoreAfter.set == ongoingSet }.maxOf { score -> score.scoreAfter.points.opponentScore })
            _mySetResults.value.add(scoreHistory.filter { it.scoreAfter.set == ongoingSet }.maxOf { score -> score.scoreAfter.points.myScore })
            _wonTeam.value = checkIfGameIsWon()
            if(_wonTeam.value == 0) {
                ongoingSet++
                scoreHistory.add(
                    ScoreEvent(
                        setWinner,
                        ScoreSnapshot(
                            ongoingSet,
                            PointScore(0, 0)
                        )
                    )
                )
            }
        } ?: run {
            _servingTeam.value = scoreHistory.last().scoringPlayer
        }
    }

    override fun undoLastScore() {
        // Turn back to previous set if we just started a new one
        if (scoreHistory[scoreHistory.lastIndex - 1].scoreAfter.set !=
            scoreHistory[scoreHistory.lastIndex].scoreAfter.set
        ) {
            ongoingSet--
            // Clear the set history that's displayed in the center of the screen
            _opponentSetResults.value.removeAt(_opponentSetResults.value.lastIndex)
            _mySetResults.value.removeAt(_mySetResults.value.lastIndex)
            //Clear the new '0-0' set start
            scoreHistory.removeAt(scoreHistory.lastIndex)
        }
        //Undo the actual last point
        scoreHistory.removeAt(scoreHistory.lastIndex)
    }

    override fun checkIfSetIsWon(): Int? {
        val opponentScore = scoreHistory.last().scoreAfter.points.opponentScore
        val myScore = scoreHistory.last().scoreAfter.points.myScore

        if (opponentScore > 20 && opponentScore - myScore > 1)
            return 1
        if (myScore > 20 && myScore - opponentScore > 1)
            return 2

        return null
    }

    fun getOpponentColor(): String {
        return sharedPreferences.getString(SETTING_OPPONENT_COLOR, Color.Red.toArgb().toHexString()) ?: Color.Red.toArgb().toHexString()
    }

    fun getMyColor(): String {
        return sharedPreferences.getString(SETTING_MY_COLOR, Color.Blue.toArgb().toHexString()) ?: Color.Blue.toArgb().toHexString()
    }
}