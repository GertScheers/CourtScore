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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.models.GameType
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme

@Composable
fun SportsChoiceScreen(navigateToGameScreen: (GameType) -> Unit) {
    Column {
        Text("What are we playing?")
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            CompactChip(
                onClick = { navigateToGameScreen(GameType.Padel) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_padel),
                        null
                    )
                })
            CompactChip(
                onClick = { navigateToGameScreen(GameType.Tennis) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_tennis),
                        null
                    )
                })
            CompactChip(
                onClick = { navigateToGameScreen(GameType.Badminton) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                        null
                    )
                })
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun SportsChoiceScreenPreview() {
    CourtScoreTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            SportsChoiceScreen { }
        }
    }
}