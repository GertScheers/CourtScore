package com.gitje.courtscorewear.logic

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TennisPadelViewModel(application: Application): BaseViewModel(application) {
    //Stores ongoing set's points
    private var _ongoingSetResults = MutableStateFlow<List<Int>>(listOf())
    val ongoingSetResults: StateFlow<List<Int>> = _ongoingSetResults

    private var lastPointScoring = emptyList<Int>()
    private var setServeStarer = 0

    override fun startNewGame() {
        _ongoingSetResults.value = listOf()
        lastPointScoring = emptyList()
        setServeStarer = 0
        super.startNewGame()
    }

    private fun checkIfPointIsWon(): Int? {
        val team1Score = ongoingScoring.count { it == 1 }
        val team2Score = ongoingScoring.count { it == 2 }

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
        if ((player == 1 && ongoingScoring.count { s -> s == 2 } == 4) ||
            (player == 2 && ongoingScoring.count { s -> s == 1 } == 4))
            ongoingScoring.removeAt(ongoingScoring.lastIndex)
        else
            ongoingScoring.add(player)

        checkIfPointIsWon()?.let { pointWonTeam ->
            _ongoingSetResults.value += pointWonTeam
            lastPointScoring = ongoingScoring.toList()
            ongoingScoring.clear()
            checkIfSetIsWon()?.let { _ ->
                _team1SetResults.value.add(_ongoingSetResults.value.count { it == 1 } )
                _team2SetResults.value.add(_ongoingSetResults.value.count { it == 2 } )
                _ongoingSetResults.value = emptyList()
                _servingTeam.value = if (setServeStarer == 1) 2 else 1
                setServeStarer = _servingTeam.value
                _wonTeam.value = checkIfGameIsWon()
            } ?: run {
                //New point, same set, serving takes new turn
                _servingTeam.value = if (_servingTeam.value == 1) 2 else 1
            }
        }
    }

    override fun checkIfSetIsWon(): Int? {
        val team1Points = _ongoingSetResults.value.count { it == 1 }
        val team2Points = _ongoingSetResults.value.count { it == 2 }

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
        if (ongoingScoring.isNotEmpty())
            ongoingScoring.removeAt(ongoingScoring.size - 1)
        else if(_ongoingSetResults.value.isNotEmpty()) {
            ongoingScoring.clear()
            ongoingScoring.addAll(lastPointScoring.toMutableList())
            _ongoingSetResults.value -= _ongoingSetResults.value.lastIndex
        } else if(_team1SetResults.value.isNotEmpty()) {
            //Undo won set, fill history with setHistory's values and continue playing 'closed set'
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