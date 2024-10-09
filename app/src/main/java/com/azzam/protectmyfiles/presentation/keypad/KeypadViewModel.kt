package com.azzam.protectmyfiles.presentation.keypad

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.domain.repository.Repository
import com.azzam.protectmyfiles.presentation.navigation.Screens
import com.azzam.protectmyfiles.util.UIEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

class KeypadViewModel(private val repository: Repository) : ViewModel() {
    private var _keypadScreenState = mutableStateOf(KeypadScreenState())
    val keypadScreenState: MutableState<KeypadScreenState> = _keypadScreenState

    private val _navigateToNextPage = Channel<String>()
    val navigateToNextPage = _navigateToNextPage.receiveAsFlow()

    private val _showErrorMessage = Channel<UIEvent.ShowStringResource>()
    val showErrorMessage = _showErrorMessage.receiveAsFlow()

    private var _enterNewPasscode = MutableStateFlow(false)

    fun updateCode(digit: String) {

        val newPassCode = _keypadScreenState.value.enteredCode.plus(digit)
        Timber.v("code : $newPassCode")
        if (newPassCode.length > 4)
            return

        _keypadScreenState.value = _keypadScreenState.value.copy(enteredCode = newPassCode)


        if (newPassCode.length == 4) {
            when (_enterNewPasscode.value) {
                true -> {
                    if (_keypadScreenState.value.isEnteringForSecondTime) {
                        if (_keypadScreenState.value.isNewCodeEqualToOldCode()) {
                            if (repository.savePasscode(_keypadScreenState.value.passCode)) {
                                _navigateToNextPage.trySend(Screens.HomeScreen.route)
                            } else {
                                _showErrorMessage.trySend(UIEvent.ShowStringResource(R.string.error_occurred))
                            }
                        } else {
                            Timber.v(_keypadScreenState.value.enteredCode)
                            Timber.v(_keypadScreenState.value.passCode)
                            _showErrorMessage.trySend(UIEvent.ShowStringResource(R.string.passcodes_does_not_match))
                        }
                    } else {
                        _keypadScreenState.value =
                            _keypadScreenState.value.copy(
                                enteredCode = "",
                                passCode = newPassCode,
                                isEnteringForSecondTime = true
                            )
                    }
                }

                false -> {
                    Timber.v("passcode : ${repository.getPasscode()}")
                    if (repository.getPasscode() == _keypadScreenState.value.enteredCode) {
                        _navigateToNextPage.trySend(Screens.HomeScreen.route)
                    } else {
                        _showErrorMessage.trySend(UIEvent.ShowStringResource(R.string.wrong_passcode))
                    }
                }
            }
        }
    }

    fun deleteDigit() {
        _keypadScreenState.value = _keypadScreenState.value.copy(
            enteredCode = _keypadScreenState.value.enteredCode.dropLast(1)
        )
        Timber.v("code : ${_keypadScreenState.value.enteredCode}")
    }

    fun setIsEnterNewPasscode(isEnterNewPasscode: Boolean) {
        _enterNewPasscode.value = isEnterNewPasscode
    }
}