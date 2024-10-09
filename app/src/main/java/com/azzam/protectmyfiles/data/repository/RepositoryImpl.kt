package com.azzam.protectmyfiles.data.repository

import android.util.Base64
import com.azzam.protectmyfiles.data.local.FileChunk
import com.azzam.protectmyfiles.data.local.MainDao
import com.azzam.protectmyfiles.data.mappers.toEncryptedEntity
import com.azzam.protectmyfiles.data.mappers.toEncryptedModel
import com.azzam.protectmyfiles.data.mappers.toPasswordEntity
import com.azzam.protectmyfiles.data.mappers.toPasswordModel
import com.azzam.protectmyfiles.data.sharedPref.SharedPrefs
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.domain.model.PasswordModel
import com.azzam.protectmyfiles.domain.repository.Repository
import com.azzam.protectmyfiles.util.CryptoManager
import com.azzam.protectmyfiles.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RepositoryImpl(
    private val mainDao: MainDao,
    private val sharedPrefs: SharedPrefs
) : Repository {
    override fun insertEncryptedModel(
        encryptedModel: EncryptedModel,
        bytes: ByteArray
    ): Flow<Resource<EncryptedModel>> = flow {
        try {
            emit(Resource.Loading())
            val chunks = mutableListOf<FileChunk>()
            val chunkSize: Int = 1024 * 1024
            val chunkCount = (bytes.size + chunkSize - 1) / chunkSize

            for (i in 0 until chunkCount) {
                val start = i * chunkSize
                val end = minOf((i + 1) * chunkSize, bytes.size)
                val chunk = bytes.copyOfRange(start, end)

                val fileChunk =
                    FileChunk(chunkIndex = i, data = chunk)
                chunks.add(fileChunk)
            }
            val encryptedEntity =
                mainDao.insertEncryptedEntityWithChunks(encryptedModel.toEncryptedEntity(), chunks)
            emit(Resource.Success(encryptedEntity.toEncryptedModel()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override fun deleteEncryptedEntityWithChunks(encryptedModelId: Int) = flow {
        try {
            emit(Resource.Loading())
            mainDao.deleteEncryptedEntityWithChunks(encryptedModelId)
            emit(Resource.Success(null))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }


    override fun getAllEncryptedModel(): Flow<Resource<List<EncryptedModel?>>> = flow {
        emit(Resource.Loading())
        try {
            val data = mainDao.getAllEncryptedEntity().map { it.toEncryptedModel() }
            emit(Resource.Success(data))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override fun getFileWithChunks(id: Int) = flow {
        try {
            emit(Resource.Loading())
            val data = mainDao.getFileWithChunks(id)
            emit(Resource.Success(data))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override fun insertPasswordModel(passwordModel: PasswordModel): Flow<Resource<PasswordModel>> = flow {
            emit(Resource.Loading())
            try {
                val id = mainDao.insertPasswordEntity(passwordModel.toPasswordEntity())
                val insertedPasswordModel = passwordModel.copy(id = id.toInt())
                emit(Resource.Success(insertedPasswordModel))
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            }
        }

    override fun deletePasswordModel(passwordModel: PasswordModel): Flow<Resource<Nothing>> = flow {
        emit(Resource.Loading())
        try {
            mainDao.deletePasswordEntity(passwordModel.toPasswordEntity())
            emit(Resource.Success(null))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override fun getAllPasswordModels(): Flow<Resource<List<PasswordModel?>>> = flow {
        emit(Resource.Loading())
        try {
            val data = mainDao.getAllPasswordEntity().map { it.toPasswordModel() }
            emit(Resource.Success(data))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override fun savePasscode(value: String): Boolean {
        try {
            val cryptoManager = CryptoManager()
            val encryptedValue = cryptoManager.encrypt(value.toByteArray())
            sharedPrefs.savePasscode(Base64.encodeToString(encryptedValue.data, Base64.DEFAULT))
            sharedPrefs.saveIV(Base64.encodeToString(encryptedValue.iv, Base64.DEFAULT))
            return true
        } catch (e: Exception) {
            Timber.e(e.message)
        }
        return false
    }

    override fun getPasscode(): String? {
        try {
            val cryptoManager = CryptoManager()
            val encryptedValue = sharedPrefs.getPasscode()
            val iv = sharedPrefs.getIV()
            if (encryptedValue != null && iv != null) {
                val decryptedValue = cryptoManager.decrypt(
                    data = Base64.decode(encryptedValue, Base64.DEFAULT),
                    iv = Base64.decode(iv, Base64.DEFAULT)
                )
                val passcode = String(decryptedValue)
                Timber.v("Passcode: $passcode")
                return passcode
            }
        } catch (e: Exception) {
            Timber.e(e.message)
        }
        return null
    }
}