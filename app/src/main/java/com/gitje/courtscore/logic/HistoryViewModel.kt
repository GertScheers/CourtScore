package com.gitje.courtscore.logic

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

const val SETTING_SHOW_BADMINTON = "SETTING_SHOW_BADMINTON"
const val SETTING_SHOW_TENNIS = "SETTING_SHOW_TENNIS"
const val SETTING_SHOW_PADEL = "SETTING_SHOW_PADEL"
const val SETTING_SHOW_FILTER = "SETTING_SHOW_FILTER"
class HistoryViewModel(
    application: Application,
    val sharedPreferences: SharedPreferences
) : AndroidViewModel(application) {

    private val _showBadminton = MutableStateFlow(sharedPreferences.getBoolean(SETTING_SHOW_BADMINTON, true))
    val showBadminton: StateFlow<Boolean> = _showBadminton
    private val _showTennis = MutableStateFlow(sharedPreferences.getBoolean(SETTING_SHOW_TENNIS, true))
    val showTennis: StateFlow<Boolean> = _showTennis
    private val _showPadel = MutableStateFlow(sharedPreferences.getBoolean(SETTING_SHOW_PADEL, true))
    val showPadel: StateFlow<Boolean> = _showPadel
    private val _showFilter = MutableStateFlow(sharedPreferences.getBoolean(SETTING_SHOW_FILTER, true))
    val showFilter: StateFlow<Boolean> = _showFilter

    fun setShowBadminton(show: Boolean) {
        sharedPreferences.edit {
            putBoolean(SETTING_SHOW_BADMINTON, show)
        }
        _showBadminton.value = show
    }

    fun setShowTennis(show: Boolean) {
        sharedPreferences.edit {
            putBoolean(SETTING_SHOW_TENNIS, show)
        }
        _showTennis.value = show
    }

    fun setShowPadel(show: Boolean) {
        sharedPreferences.edit {
            putBoolean(SETTING_SHOW_PADEL, show)
        }
        _showPadel.value = show
    }

    fun setShowFilter(show: Boolean) {
        sharedPreferences.edit {
            putBoolean(SETTING_SHOW_FILTER, show)
        }
        _showFilter.value = show
    }
}