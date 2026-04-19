package com.gitje.courtscorewear.logic

import android.app.Application
import kotlin.collections.isNotEmpty

class BadmintonViewModel(application: Application) : BaseViewModel(application) {
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
        else if(_team1SetResults.value.count() > 1) {
            //Undo won set, fill history with setHistory's values and continue playing 'closed set'
            //TODO: This needs testing
            repeat(_team1SetResults.value.last()) {
                ongoingScoring.add(1)
            }
            repeat(_team2SetResults.value.last()) {
                ongoingScoring.add(2)
            }
            _team1SetResults.value.removeAt(_team1SetResults.value.lastIndex)
            _team2SetResults.value.removeAt(_team2SetResults.value.lastIndex)
        }
    }
}