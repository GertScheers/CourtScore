package com.gitje.courtscorewear.logic

import android.app.Application
import kotlin.collections.isNotEmpty

class BadmintonViewModel(application: Application) : BaseViewModel(application) {
    override fun teamScored(player: Int) {
        _ongoingScoring.value.add(player)

        val setOver = checkIfSetIsWon()
        //setOver == null -> Continue game
        setOver?.let {
            if (it == 1) {
                _servingTeam.value = 1
            } else {
                _servingTeam.value = 2
            }
            _team1SetResults.value.add(_ongoingScoring.value.count { score -> score == 1 })
            _team2SetResults.value.add(_ongoingScoring.value.count { score -> score == 2 })
            _ongoingScoring.value.clear()
            _wonTeam.value = checkIfGameIsWon()
        } ?: run {
            _servingTeam.value = _ongoingScoring.value.last()
        }
    }

    override fun checkIfSetIsWon(): Int? {
        val team1Score = _ongoingScoring.value.count { it == 1 }
        val team2Score = _ongoingScoring.value.count { it == 1 }

        if (team1Score > 20 && team1Score - team2Score > 1)
            return 1
        if (team2Score > 20 && team2Score - team1Score > 1)
            return 2

        return null
    }

    override fun undoLastScore() {
        if (_ongoingScoring.value.isNotEmpty())
            _ongoingScoring.value.removeAt(_ongoingScoring.value.size - 1)
        else if(_team1SetResults.value.count() > 1) {
            //Undo won set, fill history with setHistory's values and continue playing 'closed set'
            //TODO: This needs testing
            repeat(_team1SetResults.value.last()) {
                _ongoingScoring.value.add(1)
            }
            repeat(_team2SetResults.value.last()) {
                _ongoingScoring.value.add(2)
            }
            _team1SetResults.value.removeAt(_team1SetResults.value.lastIndex)
            _team2SetResults.value.removeAt(_team2SetResults.value.lastIndex)
        }
    }
}