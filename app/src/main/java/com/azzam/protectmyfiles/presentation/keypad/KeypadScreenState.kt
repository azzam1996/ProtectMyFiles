package com.azzam.protectmyfiles.presentation.keypad

data class KeypadScreenState(
    val enteredCode: String = "",
    val isEnteringForSecondTime: Boolean = false,
    val passCode: String = "",
) {
    fun isNewCodeEqualToOldCode() = enteredCode == passCode
}
