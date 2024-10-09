package com.azzam.protectmyfiles.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class EncryptedEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String,
    val extension: String,
    var fileSize: Int? = null,
    val iv: ByteArray? = null,
)

data class FilesWithChunks(
    @Embedded val encryptedEntity: EncryptedEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "encryptedEntityId"
    )
    val chunks: List<FileChunk>
)

@Entity
data class FileChunk(
    val chunkIndex: Int,
    val data: ByteArray,
    var encryptedEntityId: Int? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}