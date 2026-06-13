package com.gitje.courtscorewear.presentation.composables

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.SplitSwitchButton
import androidx.wear.compose.material3.Text

@Composable
fun SettingsScreen(
    keepScreenOn: Boolean,
    changeKeepScreenOn: (Boolean) -> Unit
) {
    SplitSwitchButton(
        keepScreenOn,
        onCheckedChange = { changeKeepScreenOn(it) },
        toggleContentDescription = "Keep screen on",
        onContainerClick = { changeKeepScreenOn(!keepScreenOn) }) {
        Text("Keep screen on")
    }
}