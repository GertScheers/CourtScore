package com.gitje.courtscorewear.logic

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TennisPadelViewModel(application: Application): BaseViewModel(application) {
    //Stores ongoing set's points
    private var _ongoingSetResults = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val ongoingSetResults: StateFlow<List<Int>> = _ongoingSetResults

    private var lastPointScoring = emptyList<Int>()
    private var firstSetServer = 0

    private fun checkIfPointIsWon(): Int? {
        val team1Score = _ongoingScoring.value.count { it == 1 }
        val team2Score = _ongoingScoring.value.count { it == 1 }

        if ((team1Score == 4 && team2Score < 3)
            || team1Score == 5
        )
            return 1
        if ((team2Score == 4 && team1Score < 3)
            || team2Score == 5
        )
            return 2
        return null
    }

    override fun teamScored(player: Int) {
        //Cancel ADV for other team if they had, else add point
        if ((player == 1 && _ongoingScoring.value.count { s -> s == 2 } == 4) ||
            (player == 2 && _ongoingScoring.value.count { s -> s == 1 } == 4))
            _ongoingScoring.value.removeAt(_ongoingScoring.value.lastIndex)
        else
            _ongoingScoring.value.add(player)

        checkIfPointIsWon()?.let { pointWonTeam ->
            _ongoingSetResults.value.add(pointWonTeam)
            lastPointScoring = _ongoingScoring.value.toList()
            _ongoingScoring.value.clear()
            checkIfSetIsWon()?.let { _ ->
                _team1SetResults.value.add(_ongoingSetResults.value.count { it == 1 } )
                _team2SetResults.value.add(_ongoingSetResults.value.count { it == 2 } )
                _ongoingSetResults.value.clear()
                _wonTeam.value = checkIfGameIsWon()
            } ?: run {
                //New set, first server gets switched
                _servingTeam.value = if (firstSetServer == 1) 2 else 1
            }
        } ?: run {
            _servingTeam.value = if (_servingTeam.value == 1) 2 else 1
        }
    }

    override fun checkIfSetIsWon(): Int? {
        val team1Points = _ongoingScoring.value.count { it == 1 }
        val team2Points = _ongoingScoring.value.count { it == 2 }

        if ((team1Points == 6 && team2Points < 5) ||
            team1Points == 7
        ) {
            return 1
        }
        if((team2Points == 6 && team1Points < 5) ||
            team2Points == 7
            ) {
            return 2
        }
        return null
    }

    //TODO : Entire method needs testing
    override fun undoLastScore() {
        if (_ongoingScoring.value.isNotEmpty())
            _ongoingScoring.value.removeAt(_ongoingScoring.value.size - 1)
        else if(_ongoingSetResults.value.isNotEmpty()) {
            _ongoingScoring.value = lastPointScoring.toMutableList()
            _ongoingSetResults.value.removeAt(_ongoingSetResults.value.lastIndex)
        } else if(_team1SetResults.value.isNotEmpty()) {
            //Undo won set, fill history with setHistory's values and continue playing 'closed set'
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