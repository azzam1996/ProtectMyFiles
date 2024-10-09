package com.azzam.protectmyfiles.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import timber.log.Timber

@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEncryptedEntity(encryptedEntity: EncryptedEntity): Long

    @Delete
    fun deleteEncryptedEntity(encryptedEntity: EncryptedEntity)

    @Transaction
    @Query("SELECT * FROM EncryptedEntity")
    fun getAllEncryptedEntity(): List<EncryptedEntity>

    @Transaction
    fun insertEncryptedEntityWithChunks(
        encryptedEntity: EncryptedEntity,
        fileChunks: List<FileChunk>
    ): EncryptedEntity {
        val id = insertEncryptedEntity(encryptedEntity)
        Timber.v("encryptedEntityId: $id")
        fileChunks.forEach { chunk ->
            chunk.encryptedEntityId = id.toInt()
            val chunkId = insertFileChunk(chunk)
            Timber.v("chunkId: $chunkId")
        }
        return encryptedEntity
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFileChunk(fileChunk: FileChunk): Long

    @Delete
    fun deleteFileChunk(fileChunk: FileChunk)

    @Transaction
    @Query("SELECT * FROM EncryptedEntity WHERE id = :id")
    fun getFileWithChunks(id: Int): FilesWithChunks

    @Transaction
    fun deleteEncryptedEntityWithChunks(
        encryptedEntityId: Int,
    ) {
        val filesWithChunks = getFileWithChunks(encryptedEntityId)
        filesWithChunks.chunks.forEach { chunk ->
            deleteFileChunk(chunk)
            Timber.v("chunk: ${chunk.id}")
        }
        Timber.v("model: ${filesWithChunks.encryptedEntity.id}")
        deleteEncryptedEntity(filesWithChunks.encryptedEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPasswordEntity(passwordEntity: PasswordEntity): Long

    @Delete
    fun deletePasswordEntity(passwordEntity: PasswordEntity)

    @Transaction
    @Query("SELECT * FROM PasswordEntity")
    fun getAllPasswordEntity(): List<PasswordEntity>
}