package com.gitje.courtscorewear.presentation.composables

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.models.GameType
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SportsChoiceScreen(navigateToSettings: () -> Unit,navigateToGameScreen: (GameType) -> Unit) {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.BODY_SENSORS,
        onPermissionResult = { granted ->
            if (!granted) {
                println("TEST Permission missing")
            }
        }
    )

    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted) {
            // do something
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("What are we playing?")
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            CompactChip(
                onClick = { navigateToGameScreen(GameType.Padel) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_padel),
                        null
                    )
                },
                modifier = Modifier.height(80.dp)
            )
            CompactChip(
                onClick = { navigateToGameScreen(GameType.Tennis) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_tennis),
                        null
                    )
                },
                modifier = Modifier.height(80.dp)
            )
            CompactChip(
                onClick = { navigateToGameScreen(GameType.Badminton) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_badminton),
                        null
                    )
                },
                modifier = Modifier.height(80.dp)
            )
        }

        EdgeButton(onClick = { navigateToSettings() }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                contentDescription = "settings",
                tint = Color.Black
            )
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
            SportsChoiceScreen({ }) { }
        }
    }
}