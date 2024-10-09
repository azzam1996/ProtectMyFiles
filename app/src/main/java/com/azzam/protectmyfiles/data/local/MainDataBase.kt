package com.azzam.protectmyfiles.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [EncryptedEntity::class, FileChunk::class, PasswordEntity::class],
    version = 1
)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getMainDao(): MainDao
}