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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.gitje.courtscorewear.R
import com.gitje.courtscorewear.logic.MainViewModel
import com.gitje.courtscorewear.presentation.composables.BadmintonGameScreen
import com.gitje.courtscorewear.presentation.composables.SetsChoiceScreen
import com.gitje.courtscorewear.presentation.composables.SportsChoiceScreen
import com.gitje.courtscorewear.presentation.composables.TennisPadelGameScreen
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import org.koin.androidx.compose.koinViewModel

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
        val mainViewModel : MainViewModel = koinViewModel()
        val gameType by mainViewModel.gameType.collectAsState()
        val gameSets by mainViewModel.sets.collectAsState()

        Scaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "sports_choice"
            ) {
                composable("sports_choice") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        SportsChoiceScreen { gameType ->
                            mainViewModel.setGameType(gameType)
                            navController.navigate("sets_choice")
                        }
                    }
                }

                composable("sets_choice") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        SetsChoiceScreen { sets ->
                            mainViewModel.setSetsToPlay(sets)
                            if (gameType == GameType.Tennis
                                || gameType == GameType.Padel
                            ) {
                                navController.navigate("tennisPadelGameScreen")
                            } else {
                                navController.navigate("badmintonGameScreen")
                            }
                        }
                    }
                }

                composable("tennisPadelGameScreen") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        TennisPadelGameScreen(gameType, gameSets) {
                            navController.popBackStack(route = "sports_choice", false)
                        }
                    }
                }

                composable("badmintonGameScreen") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        BadmintonGameScreen(gameSets) {
                            navController.popBackStack(route = "sports_choice", false)
                        }
                    }
                }
            }
        }
    }
}