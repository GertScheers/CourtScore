package com.gitje.courtscorewear.logic

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.gitje.courtscore.sharedclasses.GameType
import com.gitje.courtscore.sharedclasses.PointScore
import com.gitje.courtscore.sharedclasses.ScoreEvent
import com.gitje.courtscore.sharedclasses.ScoreSnapshot


class TennisPadelViewModel(
    application: Application,
    val sharedPreferences: SharedPreferences
) : BaseViewModel(application) {
    private var setServeStarter = 0

    override fun startNewGame(sets: Int, gameType: GameType) {
        setServeStarter = 0
        super.startNewGame(sets, gameType)
    }

    private fun checkIfPointIsWon(): Int? {
        val opponentScore = scoreHistory.last().scoreAfter.gamePoints.first
        val myScore = scoreHistory.last().scoreAfter.gamePoints.second

        if ((opponentScore == 4 && myScore < 3)
            || opponentScore == 5
        )
            return 1
        if ((myScore == 4 && opponentScore < 3)
            || myScore == 5
        )
            return 2
        return null
    }

    override fun teamScored(player: Int) {
        val lastGamePoints = scoreHistory.last().scoreAfter.gamePoints
        //Cancel ADV for other team if they had, else add point
        if ((player == 1 && lastGamePoints.second == 4) ||
            (player == 2 && lastGamePoints.first == 4)
        ) {
            scoreHistory.removeAt(scoreHistory.lastIndex)
        } else {
            // Add score point to scoring team
            var opponentScore = 0
            var ownScore = 0
            // Keep at 0 if a new set has started, otherwise check last snapshot
            if (scoreHistory.last().scoreAfter.set == ongoingSet) {
                opponentScore = scoreHistory.last().scoreAfter.gamePoints.first
                ownScore = scoreHistory.last().scoreAfter.gamePoints.second
            }
            if (player == 1)
                opponentScore++
            else ownScore++

            scoreHistory.add(
                ScoreEvent(
                    player,
                    ScoreSnapshot(
                        ongoingSet,
                        points = scoreHistory.last().scoreAfter.points,
                        gamePoints = Pair(
                            opponentScore, ownScore
                        )
                    )
                )
            )
        }

        checkIfPointIsWon()?.let { playerPoint ->
            // Assign won point to player and move to next point
            var opponentPoints = scoreHistory.last().scoreAfter.points.opponentScore
            var ownPoints = scoreHistory.last().scoreAfter.points.myScore

            if (playerPoint == 1)
                opponentPoints++
            else ownPoints++

            scoreHistory.add(
                ScoreEvent(
                    playerPoint,
                    ScoreSnapshot(ongoingSet, PointScore(opponentPoints, ownPoints))
                )
            )

            checkIfSetIsWon()?.let { _ ->
                ongoingSet++
                val finalPoints = scoreHistory.last().scoreAfter.points
                _opponentSetResults.value.add(finalPoints.opponentScore)
                _mySetResults.value.add(finalPoints.myScore)
                _servingTeam.value = if (setServeStarter == 1) 2 else 1
                setServeStarter = _servingTeam.value
                scoreHistory.add(
                    ScoreEvent(
                        setServeStarter,
                        ScoreSnapshot(
                            ongoingSet,
                            PointScore(0, 0)
                        )
                    )
                )
                _wonTeam.value = checkIfGameIsWon()
            } ?: run {
                //New point, same set, serving takes new turn
                _servingTeam.value = if (_servingTeam.value == 1) 2 else 1
            }
        }
    }


    override fun undoLastScore() {
        // Turn back to previous set if we just started a new one
        val lastSet = scoreHistory.getOrNull(scoreHistory.lastIndex - 1)?.scoreAfter?.set
        if (lastSet != null &&
            lastSet != scoreHistory[scoreHistory.lastIndex].scoreAfter.set
        ) {
            ongoingSet--
            // Clear the set history that's displayed in the center of the screen
            _opponentSetResults.value.removeAt(_opponentSetResults.value.lastIndex)
            _mySetResults.value.removeAt(_mySetResults.value.lastIndex)
            //Clear the new '0-0' set start
            scoreHistory.removeAt(scoreHistory.lastIndex)
        }
        // TODO: ONGOING, CHECK THIS UNDO LOGIC, SOMETHING IS NOT RIGHT
        //Check for game points, undo the scoring there if needed
        val lastGamePoint = scoreHistory.getOrNull(scoreHistory.lastIndex - 1)?.scoreAfter?.points
        val currentGamePoint = scoreHistory[scoreHistory.lastIndex].scoreAfter.points
        if (lastGamePoint != null &&
            (lastGamePoint.opponentScore != currentGamePoint.opponentScore
                    || lastGamePoint.myScore != currentGamePoint.myScore)
            ) {
            val lastGamePointScore = scoreHistory.lastOrNull { it.scoreAfter.points.myScore != currentGamePoint.myScore
                    || it.scoreAfter.points.opponentScore != currentGamePoint.opponentScore }
            lastGamePointScore?.let {
                scoreHistory.add(
                    ScoreEvent(
                        lastGamePointScore.scoringPlayer,
                        ScoreSnapshot(
                            ongoingSet,
                            lastGamePointScore.scoreAfter.points,
                            lastGamePointScore.scoreAfter.gamePoints
                        )
                    )
                )
            }
        } else {
            //Undo the actual last point
            scoreHistory.removeAt(scoreHistory.lastIndex)
        }
    }

    override fun checkIfSetIsWon(): Int? {
        val opponentPoints = scoreHistory.last().scoreAfter.points.opponentScore
        val myPoints = scoreHistory.last().scoreAfter.points.myScore

        if ((opponentPoints == 6 && myPoints < 5) ||
            opponentPoints == 7
        ) {
            return 1
        }
        if ((myPoints == 6 && opponentPoints < 5) ||
            myPoints == 7
        ) {
            return 2
        }
        return null
    }

    fun getOpponentColor(): String {
        return sharedPreferences.getString(SETTING_OPPONENT_COLOR, Color.Red.toArgb().toHexString())
            ?: Color.Red.toArgb().toHexString()
    }

    fun getMyColor(): String {
        return sharedPreferences.getString(SETTING_MY_COLOR, Color.Blue.toArgb().toHexString())
            ?: Color.Blue.toArgb().toHexString()
    }
}