package com.azzam.protectmyfiles.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.encryptedFileList.EncryptedFileListScreen
import com.azzam.protectmyfiles.presentation.encryptedFileList.EncryptedFileListViewModel
import com.azzam.protectmyfiles.presentation.home.EncryptionScreen
import com.azzam.protectmyfiles.presentation.home.HomeViewModel
import com.azzam.protectmyfiles.presentation.keypad.KeyPadScreen
import com.azzam.protectmyfiles.presentation.keypad.KeypadViewModel
import com.azzam.protectmyfiles.presentation.savedPasswords.SavedPasswordsScreen
import com.azzam.protectmyfiles.presentation.savedPasswords.SavedPasswordsViewModel
import com.azzam.protectmyfiles.presentation.splash.SplashScreen
import com.azzam.protectmyfiles.presentation.splash.SplashViewModel
import com.azzam.protectmyfiles.util.MAIN_GRAPH_ROUTE
import com.azzam.protectmyfiles.util.NAV_KEY_ENTER_NEW_PASSCODE
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        NavHost(
            navController = appState.navController,
            startDestination = Screens.SplashScreen.route,
            route = MAIN_GRAPH_ROUTE
        ) {
            composable(Screens.SplashScreen.route) {
                val splashViewModel = koinViewModel<SplashViewModel>()
                SplashScreen(appState, splashViewModel)
            }

            composable(
                route = Screens.KeyPadScreen.route + "{$NAV_KEY_ENTER_NEW_PASSCODE}/",
                arguments = listOf(
                    navArgument(NAV_KEY_ENTER_NEW_PASSCODE) {
                        type = NavType.BoolType
                        nullable = false
                        defaultValue = false
                    }
                )
            ) {
                val enterNewPasscode = it.arguments?.getBoolean(NAV_KEY_ENTER_NEW_PASSCODE)
                val keypadViewModel = koinViewModel<KeypadViewModel>()
                keypadViewModel.setIsEnterNewPasscode(enterNewPasscode ?: false)
                KeyPadScreen(appState, keypadViewModel)
            }

            composable(Screens.HomeScreen.route) {
                val encryptionViewModel = koinViewModel<HomeViewModel>()
                EncryptionScreen(appState, encryptionViewModel)
            }

            composable(Screens.EncryptedFileListScreen.route) {
                val encryptedFileListViewModel = koinViewModel<EncryptedFileListViewModel>()
                EncryptedFileListScreen(appState, encryptedFileListViewModel)
            }

            composable(Screens.SavedPasswordsScreen.route) {
                val savedPasswordsViewModel = koinViewModel<SavedPasswordsViewModel>()
                SavedPasswordsScreen(appState, savedPasswordsViewModel)
            }
        }
    }
}