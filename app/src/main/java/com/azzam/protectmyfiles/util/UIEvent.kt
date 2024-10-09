package com.azzam.protectmyfiles.util

import androidx.annotation.StringRes

sealed class UIEvent {
    data class ShowStringResource(@StringRes val message: Int) : UIEvent()
    data class ShowStringMessage(val message: String?) : UIEvent()
}