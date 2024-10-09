package com.azzam.protectmyfiles.util

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.azzam.protectmyfiles.presentation.navigation.Screens


fun NavHostController.navOptionsNoBack() = navOptions {
    val startDestination: String =
        this@navOptionsNoBack.graph.route
            ?: this@navOptionsNoBack.graph.findStartDestination().route
            ?: Screens.SplashScreen.route

    popUpTo(startDestination) {
        inclusive = true
    }
    launchSingleTop = true
}

fun NavHostController.navOptionsHomeBack() = navOptions {
    popUpTo(Screens.HomeScreen.route) {
        inclusive = false
    }
}

const val MAIN_GRAPH_ROUTE = "main_graph"
const val NAV_KEY_ENTER_NEW_PASSCODE = "enterNewPasscode"
