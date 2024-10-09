package com.azzam.protectmyfiles.domain.repository

import com.azzam.protectmyfiles.data.local.FilesWithChunks
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.domain.model.PasswordModel
import com.azzam.protectmyfiles.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun insertEncryptedModel(encryptedModel: EncryptedModel, bytes: ByteArray): Flow<Resource<EncryptedModel>>
    fun deleteEncryptedEntityWithChunks(encryptedModelId: Int): Flow<Resource<Nothing>>
    fun getAllEncryptedModel(): Flow<Resource<List<EncryptedModel?>>>
    fun getFileWithChunks(id: Int): Flow<Resource<FilesWithChunks>>

    fun insertPasswordModel(passwordModel: PasswordModel): Flow<Resource<PasswordModel>>
    fun deletePasswordModel(passwordModel: PasswordModel): Flow<Resource<Nothing>>
    fun getAllPasswordModels(): Flow<Resource<List<PasswordModel?>>>

    fun savePasscode(value: String): Boolean
    fun getPasscode(): String?
}