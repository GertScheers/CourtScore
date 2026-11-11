package com.gitje.courtscorewear.logic

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import com.gitje.courtscore.sharedclasses.Game
import com.gitje.courtscore.sharedclasses.GameType
import com.gitje.courtscore.sharedclasses.PointScore
import com.gitje.courtscore.sharedclasses.ScoreEvent
import com.gitje.courtscore.sharedclasses.ScoreSnapshot
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

    // Stores past finished set's results
    protected var _opponentSetResults = MutableStateFlow(mutableListOf<Int>())
    val opponentSetResults: StateFlow<List<Int>> = _opponentSetResults
    protected var _mySetResults = MutableStateFlow(mutableListOf<Int>())
    val mySetResults: StateFlow<List<Int>> = _mySetResults
    protected var _wonTeam = MutableStateFlow(0)
    val wonTeam: StateFlow<Int> = _wonTeam
    protected var _servingTeam = MutableStateFlow(0)
    val servingTeam: StateFlow<Int> = _servingTeam

    protected var setsToPlay = 0
    protected var ongoingGameType = GameType.Tennis

    //region New scoring logic for full history
    val scoreHistory: SnapshotStateList<ScoreEvent> = SnapshotStateList()
    var ongoingSet = 0
    //endregion

    fun setServingTeam(server: Int) {
        _servingTeam.value = server
    }

    fun checkIfGameIsWon(): Int {
        var opponentWonSets = 0
        var myWonSets = 0
        _opponentSetResults.value.forEachIndexed { index, score ->
            if (score > _mySetResults.value[index]) {
                opponentWonSets++
            } else {
                myWonSets++
            }
        }

        val wonTeam = if (opponentWonSets > (setsToPlay / 2)) {
            // Opponent Wins
            1
        } else if (myWonSets > (setsToPlay / 2)) {
            // I/We Win(s)
            2
        } else {
            0
        }
        if (wonTeam > 0)
            sendResultToWatch()

        return wonTeam
    }

    open fun startNewGame(sets: Int, gameType: GameType) {
        scoreHistory.clear()
        scoreHistory.add(
            ScoreEvent(
                _servingTeam.value,
                ScoreSnapshot(0, PointScore(0, 0))
            )
        )
        _wonTeam.value = 0
        _servingTeam.value = 0
        _opponentSetResults.value = mutableListOf()
        _mySetResults.value = mutableListOf()
        setsToPlay = sets
        ongoingGameType = gameType
        ongoingSet = 0
    }

    fun sendResultToWatch() {
        val result = Game(
            LocalDateTime.now(),
            scoreHistory,
            _wonTeam.value,
            ongoingGameType
        )
        val test = WearDataSyncer(context = application)
        test.syncDataToPhone(result)
    }
}