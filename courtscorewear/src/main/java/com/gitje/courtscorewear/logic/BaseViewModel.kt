package com.gitje.courtscorewear.logic

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gitje.courtscorewear.util.HealthServicesManager
import com.gitje.courtscorewear.util.MeasureMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    application: Application,
    val healthServicesManager: HealthServicesManager
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
    protected var _heartRate = MutableStateFlow(0.0)
    val heartRate: StateFlow<Double> = _heartRate

    protected var setsToPlay = 0

    init {
        viewModelScope.launch {
            if (healthServicesManager.hasHeartRateCapability()) {
                startMonitoringHealthServices()
            }
        }
    }

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
        return if (team1WonSets > (setsToPlay / 2)) {
            // Team 1 Wins
            1
        } else if (team2WonSets > (setsToPlay / 2)) {
            // Team2 Wins
            2
        } else {
            0
        }
    }

    open fun startNewGame(sets: Int) {
        ongoingScoring.clear()
        _wonTeam.value = 0
        _servingTeam.value = 0
        _team1SetResults.value = mutableListOf()
        _team2SetResults.value = mutableListOf()
        setsToPlay = sets
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startMonitoringHealthServices() {
        healthServicesManager
            .heartRateMeasureFlow()
            .collect { measureMessage ->
                when (measureMessage) {
                    is MeasureMessage.MeasureData -> {
                        val latestHeartRateValue = measureMessage.data.last().value
                        _heartRate.value = latestHeartRateValue
                    }

                    else -> { }
                }
            }
    }
}
