package com.azzam.protectmyfiles.presentation

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.azzam.protectmyfiles.presentation.navigation.Screens
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    context: Context = LocalContext.current
) =
    remember(drawerState, navController, coroutineScope) {
        AppState(drawerState, navController, coroutineScope, context)
    }

class AppState(
    val drawerState: DrawerState,
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val context: Context
) {
    var selectedBottomNavItemRoute by mutableStateOf(Screens.HomeScreen.route)
    var showBottomBarBySettingValueFromScreen by mutableStateOf(false)

    fun navigate(
        route: String,
        navOptions: NavOptions? = null,
    ) {
        navController.navigate(
            route,
            navOptions
        )
    }
}