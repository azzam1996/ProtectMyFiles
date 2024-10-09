package com.azzam.protectmyfiles.domain.model

data class PasswordModel(
    var id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
    val iv: ByteArray? = null,
)
