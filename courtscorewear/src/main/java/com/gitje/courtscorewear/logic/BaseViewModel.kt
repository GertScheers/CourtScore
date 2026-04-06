package com.gitje.courtscorewear.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gitje.courtscorewear.models.GameType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected var _gameType = MutableStateFlow(GameType.None)
    val gameType : StateFlow<GameType> = _gameType
    //Track ongoing points
    protected var _ongoingScoring = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val ongoingScoring: StateFlow<List<Int>> = _ongoingScoring
    //Stores past finished set's results
    protected var _team1SetResults = MutableStateFlow(mutableListOf(0))
    val team1SetResults: StateFlow<List<Int>> = _team1SetResults
    protected var _team2SetResults = MutableStateFlow(mutableListOf(0))
    val team2SetResults: StateFlow<List<Int>> = _team2SetResults
    protected var _wonTeam = MutableStateFlow(0)
    val wonTeam: StateFlow<Int> = _wonTeam
    protected var _servingTeam = MutableStateFlow(0)
    val servingTeam: StateFlow<Int> = _servingTeam

    protected var setsToPlay = 0

    fun setGameType(gameTypeToPlay: GameType) {
        _gameType.value = gameTypeToPlay
    }

    fun configureSetsToPlay(sets: Int) {
        setsToPlay = sets
    }

    fun setServingTeam(server: Int) {
        _servingTeam.value = server
    }

    fun checkIfGameIsWon(): Int {
        var team1WonSets = 0
        var team2WonSets = 0
        _team1SetResults.value.forEachIndexed { index, score ->
            if(score > _team2SetResults.value[index])
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

    abstract fun teamScored(player: Int)
    abstract fun checkIfSetIsWon(): Int?
    abstract fun undoLastScore()
}