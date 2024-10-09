package com.azzam.protectmyfiles.presentation.encryptedFileList

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.domain.repository.Repository
import com.azzam.protectmyfiles.util.CryptoManager
import com.azzam.protectmyfiles.util.Resource
import com.azzam.protectmyfiles.util.UIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.UUID

class EncryptedFileListViewModel(private val repository: Repository) : ViewModel() {
    var encryptedFilesState by mutableStateOf(EncryptedFileListState())
        private set

    private val _showErrorMessage = Channel<UIEvent.ShowStringMessage>()
    val showErrorMessage = _showErrorMessage.receiveAsFlow()


    fun getEncryptedFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllEncryptedModel().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        encryptedFilesState = encryptedFilesState.copy(isLoading = false)
                        _showErrorMessage.send(UIEvent.ShowStringMessage(result.message))
                    }

                    is Resource.Loading -> {
                        encryptedFilesState = encryptedFilesState.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        encryptedFilesState = encryptedFilesState.copy(
                            isLoading = false,
                            encryptedFiles = result.data ?: emptyList()
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun showFileFromDB(context: Context, encryptedModel: EncryptedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            encryptedModel.id?.let { id ->
                repository.getFileWithChunks(id).onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            encryptedFilesState =
                                encryptedFilesState.copy(showDecryptionAnimation = false)
                            _showErrorMessage.trySend(UIEvent.ShowStringMessage(result.message))
                        }

                        is Resource.Loading -> {
                            encryptedFilesState =
                                encryptedFilesState.copy(showDecryptionAnimation = true)
                        }

                        is Resource.Success -> {
                            result.data?.let { file ->
                                val file2 = File(
                                    context.cacheDir,
                                    file.encryptedEntity.name + UUID.randomUUID() + file.encryptedEntity.extension
                                )
                                val bytes = file.chunks
                                    .sortedBy { it.chunkIndex }
                                    .flatMap { it.data.toList() }
                                    .toByteArray()

                                Timber.v("bytes: ${bytes.size}")
                                Timber.v("name: ${file.encryptedEntity.name}")
                                Timber.v("iv: ${file.encryptedEntity.iv?.size}")
                                file.chunks.forEach { chunk ->
                                    Timber.v("chunk: ${chunk.id}")
                                }
                                bytes.let {
                                    file.encryptedEntity.iv?.let {
                                        val decryptedBytes =
                                            CryptoManager().decrypt(bytes, file.encryptedEntity.iv)
                                        file2.outputStream().use { stream ->
                                            stream.write(decryptedBytes)
                                        }

                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "com.azzam.protectmyfiles.fileprovider",
                                            file2
                                        )
                                        withContext(Dispatchers.Main) {
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            if (
                                                context.packageManager != null &&
                                                intent.resolveActivity(context.packageManager) != null
                                            ) {
                                                context.startActivity(intent)
                                            }
                                        }
                                    }
                                }
                            }
                            encryptedFilesState =
                                encryptedFilesState.copy(showDecryptionAnimation = false)
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    fun deleteFileFromDB(encryptedModel: EncryptedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            encryptedModel.id?.let { id ->
                repository.deleteEncryptedEntityWithChunks(id).onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            encryptedFilesState = encryptedFilesState.copy(isLoading = false)
                            _showErrorMessage.send(UIEvent.ShowStringMessage(result.message))
                        }

                        is Resource.Loading -> {
                            encryptedFilesState = encryptedFilesState.copy(isLoading = true)
                        }

                        is Resource.Success -> {
                            encryptedFilesState = encryptedFilesState.copy(isLoading = false)
                            getEncryptedFiles()
                        }
                    }

                }.launchIn(this)
            }
        }
    }
}