package com.gitje.courtscorewear.logic

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel

const val SETTING_KEEP_SCREEN_ON = "keep_screen_on"
const val SETTING_TEAM_1_COLOR = "setting_team_1_color"
const val SETTING_TEAM_2_COLOR = "setting_team_2_color"

class SettingsViewModel(
    application: Application,
    val sharedPreferences: SharedPreferences)
    : AndroidViewModel(application) {

    fun getKeepScreenOn(): Boolean {
        return sharedPreferences.getBoolean(SETTING_KEEP_SCREEN_ON, false)
    }

    fun getTeam1Color(): String {
        val fallback = Color.Red.toArgb().toHexString()
        return sharedPreferences.getString(SETTING_TEAM_1_COLOR, fallback) ?: fallback
    }

    fun getTeam2Color(): String {
        val fallback = Color.Blue.toArgb().toHexString()
        return sharedPreferences.getString(SETTING_TEAM_2_COLOR, fallback) ?: fallback
    }

    fun setKeepScreenOn(on: Boolean) {
        sharedPreferences.edit {
            putBoolean(SETTING_KEEP_SCREEN_ON, on)
        }
    }

    fun setTeam1Color(color: String) {
        sharedPreferences.edit {
            putString(SETTING_TEAM_1_COLOR, color)
        }
    }

    fun setTeam2Color(color: String) {
        sharedPreferences.edit {
            putString(SETTING_TEAM_2_COLOR, color)
        }
    }
}