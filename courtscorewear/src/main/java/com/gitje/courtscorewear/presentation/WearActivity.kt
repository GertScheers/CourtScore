/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.gitje.courtscorewear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.logic.BaseViewModel
import com.gitje.courtscorewear.logic.TennisPadelViewModel
import com.gitje.courtscorewear.models.GameType
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
        val baseViewModel: BaseViewModel = koinViewModel()
        val badmintonViewModel: BadmintonViewModel = koinViewModel()
        val tennisPadelViewModel: TennisPadelViewModel = koinViewModel()
        val gameType by baseViewModel.gameType.collectAsState()

        Scaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "sports_choice",
            ) {
                composable("sports_choice") {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        SportsChoiceScreen { gameType ->
                            baseViewModel.setGameType(gameType)
                            navController.navigate("sets_choice")
                        }
                    }
                }

                composable("sets_choice") {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        SetsChoiceScreen { sets ->
                            if (gameType == GameType.Tennis ||
                                gameType == GameType.Padel
                            ) {
                                // TODO: tennisViewModel.setSetsToPlay(sets)
                                navController.navigate("tennisPadelGameScreen")
                            } else {
                                badmintonViewModel.configureSetsToPlay(sets)
                                navController.navigate("badmintonGameScreen")
                            }
                        }
                    }
                }

                composable("tennisPadelGameScreen") {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        tennisPadelViewModel.startNewGame()
                        TennisPadelGameScreen {
                            navController.popBackStack(route = "sports_choice", false)
                        }
                    }
                }

                composable("badmintonGameScreen") {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        badmintonViewModel.startNewGame()
                        BadmintonGameScreen {
                            navController.popBackStack(route = "sports_choice", false)
                        }
                    }
                }
            }
        }
    }
}
