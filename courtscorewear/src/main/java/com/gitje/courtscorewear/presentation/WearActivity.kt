/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.gitje.courtscorewear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme

class WearActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    CourtScoreTheme {
        val navController = rememberSwipeDismissableNavController()
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "sports_choice"
        ) {
            // TODO: build navigation graph
            composable("sports_choice") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    SportsChoice { gt ->
                        navController.navigate("game_screen/$gt")
                    }
                }
            }

            composable("game_screen/{gt}") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    GameScreen(
                        gameType = GameType.valueOf(
                            it.arguments?.getString("gameType") ?: "Tennis"
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SportsChoice(navigateToGameScreen: (GameType) -> Unit) {
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
fun DefaultPreview() {
    CourtScoreTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            SportsChoice { }
        }
    }
}