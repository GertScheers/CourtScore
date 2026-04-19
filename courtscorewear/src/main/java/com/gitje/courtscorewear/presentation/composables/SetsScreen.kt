package com.gitje.courtscorewear.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme

@Composable
fun SetsChoiceScreen(
    setSets: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Best of? (sets)")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CompactChip(
                onClick = { setSets(1) },
                label = {
                    Text("1")
                })
            CompactChip(
                onClick = { setSets(3) },
                label = {
                    Text("3")
                })
            CompactChip(
                onClick = { setSets(5) },
                label = {
                    Text("5")
                })
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun SetsScreenPreview() {
    CourtScoreTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            SetsChoiceScreen { }
        }
    }
}