package com.azzam.protectmyfiles.domain.model

import com.azzam.protectmyfiles.util.FileType

data class EncryptedModel(
    var id: Int? = null,
    val name: String,
    val fileType: FileType,
    var fileSize: Int? = null,
    val iv: ByteArray? = null,
)