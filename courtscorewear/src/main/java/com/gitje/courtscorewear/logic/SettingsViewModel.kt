package com.gitje.courtscorewear.logic

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel

const val SETTING_KEEP_SCREEN_ON = "keep_screen_on"
const val SETTING_OPPONENT_COLOR = "setting_team_1_color"
const val SETTING_MY_COLOR = "setting_team_2_color"

class SettingsViewModel(
    application: Application,
    val sharedPreferences: SharedPreferences)
    : AndroidViewModel(application) {

    fun getKeepScreenOn(): Boolean {
        return sharedPreferences.getBoolean(SETTING_KEEP_SCREEN_ON, false)
    }

    fun getOpponentColor(): String {
        val fallback = Color.Red.toArgb().toHexString()
        return sharedPreferences.getString(SETTING_OPPONENT_COLOR, fallback) ?: fallback
    }

    fun getMyColor(): String {
        val fallback = Color.Blue.toArgb().toHexString()
        return sharedPreferences.getString(SETTING_MY_COLOR, fallback) ?: fallback
    }

    fun setKeepScreenOn(on: Boolean) {
        sharedPreferences.edit {
            putBoolean(SETTING_KEEP_SCREEN_ON, on)
        }
    }

    fun setOpponentColor(color: String) {
        sharedPreferences.edit {
            putString(SETTING_OPPONENT_COLOR, color)
        }
    }

    fun setMyColor(color: String) {
        sharedPreferences.edit {
            putString(SETTING_MY_COLOR, color)
        }
    }
}