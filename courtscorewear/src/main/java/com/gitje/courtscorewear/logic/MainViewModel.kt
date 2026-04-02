package com.gitje.courtscorewear.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gitje.courtscorewear.presentation.GameType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var _sets = MutableStateFlow(0)
    val sets : StateFlow<Int> = _sets

    private var _gameType = MutableStateFlow(GameType.None)
    val gameType : StateFlow<GameType> = _gameType

    fun setSetsToPlay(sets: Int) {
        _sets.value = sets
    }

    fun setGameType(gameTypeToPlay: GameType) {
        _gameType.value = gameTypeToPlay
    }
}