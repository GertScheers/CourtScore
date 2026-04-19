package com.gitje.courtscorewear.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

@Composable
fun GameFinishedScreen(wonTeam: Int, backToStart: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Team ${if (wonTeam == 1) "1" else "2"} wins!", fontSize = 26.sp)
        Text(if (wonTeam == 1) "Better luck next time!" else "Congratulations!")
        Button(onClick = { backToStart() }) { Text("Return") }
    }
}

@Composable
fun ServerPickerScreen(setServer: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text("Who will start?")
        Button(
            onClick = { setServer(1) },
            modifier = Modifier.fillMaxWidth(0.6f),
        ) { Text("They/(s)he") }
        Button(
            onClick = { setServer(2) },
            modifier = Modifier.fillMaxWidth(0.6f),
        ) { Text("We/me") }
    }
}