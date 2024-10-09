package com.azzam.protectmyfiles.util.extensions

import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat


fun ComponentActivity.fullScreen() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun ComponentActivity.lightStatusBarIcons() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = false
    }
}

fun ComponentActivity.darkStatusBarIcons() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = true
    }
}