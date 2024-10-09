package com.azzam.protectmyfiles.presentation.encryptedFileList

import com.azzam.protectmyfiles.domain.model.EncryptedModel

data class EncryptedFileListState(
    val isLoading: Boolean = false,
    val showDecryptionAnimation: Boolean = false,
    val encryptedFiles: List<EncryptedModel?> = emptyList(),
)
