package com.azzam.protectmyfiles.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String,
    val username: String,
    val password: ByteArray,
    val iv: ByteArray,
)
