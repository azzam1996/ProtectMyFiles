package com.azzam.protectmyfiles.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.azzam.protectmyfiles.presentation.customViews.CustomBottomNav
import com.azzam.protectmyfiles.presentation.navigation.Navigation
import com.azzam.protectmyfiles.presentation.navigation.Screens
import com.azzam.protectmyfiles.presentation.ui.theme.ProtectMyFilesTheme
import com.azzam.protectmyfiles.util.extensions.fullScreen
import com.azzam.protectmyfiles.util.extensions.lightStatusBarIcons
import com.azzam.protectmyfiles.util.extensions.mainBG
import com.azzam.protectmyfiles.util.navOptionsHomeBack
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var appState: AppState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        fullScreen()
        lightStatusBarIcons()
        setContent {
            ProtectMyFilesTheme {
                appState = rememberAppState()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .mainBG()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        Navigation(
                            appState = appState,
                            modifier = Modifier
                                .weight(1f)
                        )

                        CustomBottomNav(
                            appState = appState,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) { item ->
                            Timber.v(item.route)
                            Timber.v(appState.selectedBottomNavItemRoute)
                            if (item.route == Screens.HomeScreen.route) {
                                if (appState.selectedBottomNavItemRoute != Screens.HomeScreen.route) {
                                    appState.navController.navigateUp()
                                }
                            } else {
                                appState.navigate(
                                    route = item.route,
                                    navOptions = appState.navController.navOptionsHomeBack()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}