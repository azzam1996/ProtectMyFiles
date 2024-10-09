package com.azzam.protectmyfiles.presentation.savedPasswords

import com.azzam.protectmyfiles.domain.model.PasswordModel

data class SavedPasswordsListState(
    val isLoading: Boolean = false,
    val savedPasswords: List<PasswordModel?> = emptyList(),
)
