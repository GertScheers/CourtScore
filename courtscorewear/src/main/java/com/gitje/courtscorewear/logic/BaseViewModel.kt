package com.gitje.courtscorewear.logic

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import com.gitje.courtscorewear.models.Game
import com.gitje.courtscorewear.models.GameType
import com.gitje.courtscorewear.util.WearDataSyncer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

abstract class BaseViewModel(
    application: Application,
) : AndroidViewModel(application) {
    abstract fun teamScored(player: Int)
    abstract fun checkIfSetIsWon(): Int?
    abstract fun undoLastScore()

    // Track ongoing points
    val ongoingScoring: SnapshotStateList<Int> = SnapshotStateList()

    // Stores past finished set's results
    protected var _team1SetResults = MutableStateFlow(mutableListOf<Int>())
    val team1SetResults: StateFlow<List<Int>> = _team1SetResults
    protected var _team2SetResults = MutableStateFlow(mutableListOf<Int>())
    val team2SetResults: StateFlow<List<Int>> = _team2SetResults
    protected var _wonTeam = MutableStateFlow(0)
    val wonTeam: StateFlow<Int> = _wonTeam
    protected var _servingTeam = MutableStateFlow(0)
    val servingTeam: StateFlow<Int> = _servingTeam

    protected var setsToPlay = 0
    protected var ongoingGameType = GameType.Tennis

    fun setServingTeam(server: Int) {
        _servingTeam.value = server
    }

    fun checkIfGameIsWon(): Int {
        var team1WonSets = 0
        var team2WonSets = 0
        _team1SetResults.value.forEachIndexed { index, score ->
            if (score > _team2SetResults.value[index]) {
                team1WonSets++
            } else {
                team2WonSets++
            }
        }

        val wonTeam = if (team1WonSets > (setsToPlay / 2)) {
            // Team 1 Wins
            1
        } else if (team2WonSets > (setsToPlay / 2)) {
            // Team2 Wins
            2
        } else {
            0
        }
        if (wonTeam > 0)
            sendResultToWatch()

        return wonTeam
    }

    open fun startNewGame(sets: Int, gameType: GameType) {
        ongoingScoring.clear()
        _wonTeam.value = 0
        _servingTeam.value = 0
        _team1SetResults.value = mutableListOf()
        _team2SetResults.value = mutableListOf()
        setsToPlay = sets
        ongoingGameType = gameType
    }

    fun sendResultToWatch() {
        val result = Game(
            LocalDateTime.now(),
            _team1SetResults.value,
            _team2SetResults.value,
            _wonTeam.value,
            ongoingGameType
        )
        val test = WearDataSyncer(context = application)
        test.syncDataToPhone(result)
    }
}