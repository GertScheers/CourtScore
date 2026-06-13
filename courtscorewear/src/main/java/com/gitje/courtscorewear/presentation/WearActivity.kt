/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.gitje.courtscorewear.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.gitje.courtscorewear.logic.BadmintonViewModel
import com.gitje.courtscorewear.logic.TennisPadelViewModel
import com.gitje.courtscorewear.models.GameType
import com.gitje.courtscorewear.presentation.composables.BadmintonGameScreen
import com.gitje.courtscorewear.presentation.composables.SetsChoiceScreen
import com.gitje.courtscorewear.presentation.composables.SettingsScreen
import com.gitje.courtscorewear.presentation.composables.SportsChoiceScreen
import com.gitje.courtscorewear.presentation.composables.TennisPadelGameScreen
import com.gitje.courtscorewear.presentation.theme.CourtScoreTheme
import org.koin.androidx.compose.koinViewModel
import androidx.core.content.edit

const val SETTING_KEEP_SCREEN_ON = "keep_screen_on"

class WearActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        val keepScreenOn = sharedPref.getBoolean(SETTING_KEEP_SCREEN_ON, false)
        installSplashScreen()

        super.onCreate(savedInstanceState)

        if (keepScreenOn)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp(
                keepScreenOn,
                changeKeepScreenOn = {
                    sharedPref.edit {
                        putBoolean(SETTING_KEEP_SCREEN_ON, it)
                    }
                    recreate()
                },
                changeTeam1Color = {

                },
                changeTeam2Color = {

                }
            )
        }
    }

    @Composable
    fun WearApp(
        keepScreenOn: Boolean,
        changeKeepScreenOn: (Boolean) -> Unit,
        changeTeam1Color: (String) -> Unit,
        changeTeam2Color: (String) -> Unit
    ) {
        CourtScoreTheme {
            val navController = rememberSwipeDismissableNavController()
            val badmintonViewModel: BadmintonViewModel = koinViewModel()
            val tennisPadelViewModel: TennisPadelViewModel = koinViewModel()
            var currentGameType by remember { mutableStateOf(GameType.Tennis) }

            Scaffold {
                SwipeDismissableNavHost(
                    navController = navController,
                    startDestination = "sports_choice",
                ) {
                    composable("sports_choice") {
                        WearContainer {
                            SportsChoiceScreen(
                                navigateToSettings = {
                                    navController.navigate("settings")
                                },
                                navigateToGameScreen = { gameType ->
                                    currentGameType = gameType
                                    navController.navigate("sets_choice")
                                })
                        }
                    }

                    composable("sets_choice") {
                        WearContainer {
                            SetsChoiceScreen { sets ->
                                if (currentGameType == GameType.Tennis ||
                                    currentGameType == GameType.Padel
                                ) {
                                    tennisPadelViewModel.startNewGame(sets)
                                    navController.navigate("tennisPadelGameScreen")
                                } else {
                                    badmintonViewModel.startNewGame(sets)
                                    navController.navigate("badmintonGameScreen")
                                }
                            }
                        }
                    }

                    composable("tennisPadelGameScreen") {
                        WearContainer {
                            TennisPadelGameScreen(currentGameType) {
                                navController.popBackStack(route = "sports_choice", false)
                            }
                        }
                    }

                    composable("badmintonGameScreen") {
                        WearContainer {
                            BadmintonGameScreen {
                                navController.popBackStack(route = "sports_choice", false)
                            }
                        }
                    }

                    composable("settings") {
                        WearContainer {
                            SettingsScreen(
                                keepScreenOn,
                                changeKeepScreenOn,
                                changeTeam1Color,
                                changeTeam2Color
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun WearContainer(content: @Composable () -> Unit) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
