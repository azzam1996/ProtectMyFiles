package com.azzam.protectmyfiles.presentation.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azzam.protectmyfiles.domain.repository.Repository
import com.azzam.protectmyfiles.presentation.navigation.Screens
import com.azzam.protectmyfiles.util.DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel(private val repository: Repository) : ViewModel() {
    private val _navigateToNextScreen = mutableStateOf<String?>(null)
    val navigateToNextScreen: State<String?> = _navigateToNextScreen

    init {
        navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        viewModelScope.launch {
            var nextScreen = Screens.KeyPadScreen.withArgs(false.toString())
            val passcode = repository.getPasscode()
            Timber.v("passcode : $passcode")
            if (passcode == null) {
                nextScreen = Screens.KeyPadScreen.withArgs(true.toString())
            }
            delay(DELAY)
            Timber.v("navigateToNextScreen : $nextScreen")
            _navigateToNextScreen.value = nextScreen
        }
    }

    fun setNavigateToNextScreen(route: String?) {
        _navigateToNextScreen.value = route
    }
}