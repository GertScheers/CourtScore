package com.gitje.courtscorewear.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gitje.courtscorewear.models.GameType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private var _gameType = MutableStateFlow(GameType.None)
    val gameType : StateFlow<GameType> = _gameType

    fun setGameType(gameTypeToPlay: GameType) {
        _gameType.value = gameTypeToPlay
    }
}