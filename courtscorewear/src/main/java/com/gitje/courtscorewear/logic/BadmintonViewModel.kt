package com.gitje.courtscorewear.logic

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.isNotEmpty

class BadmintonViewModel(application: Application) : BaseViewModel(application) {
    private var _scoreHistory = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val scoreHistory: StateFlow<List<Int>> = _scoreHistory
    private var _team1SetHistory = MutableStateFlow(mutableListOf(0))
    val team1SetHistory: StateFlow<List<Int>> = _team1SetHistory
    private var _team2SetHistory = MutableStateFlow(mutableListOf(0))
    val team2SetHistory: StateFlow<List<Int>> = _team2SetHistory
    private var _wonPlayer = MutableStateFlow(0)
    val wonPlayer: StateFlow<Int> = _wonPlayer
    private var _servingTeam = MutableStateFlow(0)
    val servingTeam: StateFlow<Int> = _servingTeam

    private var setsToPlay = 0

    fun configureSetsToPlay(sets: Int) {
        setsToPlay = sets
    }

    fun setServingTeam(server: Int) {
        _servingTeam.value = server
    }

    fun teamScored(player: Int) {
        _scoreHistory.value.add(player)

        val setOver = checkIfSetIsWon()
        //setOver == null -> Continue game
        setOver?.let {
            if (it == 1) {
                _servingTeam.value = 1
            } else {
                _servingTeam.value = 2
            }
            _team1SetHistory.value.add(_scoreHistory.value.count { score -> score == 1 })
            _team2SetHistory.value.add(_scoreHistory.value.count { score -> score == 2 })
            _scoreHistory.value.clear()
        } ?: run {
            _servingTeam.value = _scoreHistory.value.last()
        }

        _wonPlayer.value = checkIfGameIsWon()
    }

    fun checkIfGameIsWon(): Int {
        var team1WonSets = 0
        var team2WonSets = 0
        _team1SetHistory.value.forEachIndexed { index, score ->
            if(score > _team2SetHistory.value[index])
                team1WonSets++
            else
                team2WonSets++
        }
        return if (team1WonSets > (setsToPlay / 2)) {
            //Team 1 Wins
            1
        } else if (team2WonSets == (setsToPlay / 2)) {
            //Team2 Wins
            2
        } else 0
    }

    fun checkIfSetIsWon(): Int? {
        val team1Score = _scoreHistory.value.count { it == 1 }
        val team2Score = _scoreHistory.value.count { it == 1 }

        if (team1Score > 20 && team1Score - team2Score > 1)
            return 1
        if (team2Score > 20 && team2Score - team1Score > 1)
            return 2

        return null
    }

    fun undoLastScore() {
        if (_scoreHistory.value.isNotEmpty())
            _scoreHistory.value.removeAt(_scoreHistory.value.size - 1)
        else if(_team1SetHistory.value.count() > 1) {
            //Undo won set, fill history with setHistory's values and continue playing 'closed set'
            //TODO: This needs testing
            repeat(_team1SetHistory.value.last()) {
                _scoreHistory.value.add(1)
            }
            repeat(_team2SetHistory.value.last()) {
                _scoreHistory.value.add(2)
            }
            _team1SetHistory.value.removeAt(_team1SetHistory.value.lastIndex)
            _team2SetHistory.value.removeAt(_team2SetHistory.value.lastIndex)
        }
    }
}