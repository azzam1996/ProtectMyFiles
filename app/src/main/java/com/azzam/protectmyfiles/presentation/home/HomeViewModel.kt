package com.azzam.protectmyfiles.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.domain.repository.Repository
import com.azzam.protectmyfiles.util.CryptoManager
import com.azzam.protectmyfiles.util.FileMetadata
import com.azzam.protectmyfiles.util.FileType
import com.azzam.protectmyfiles.util.JPG_FORMAT
import com.azzam.protectmyfiles.util.Resource
import com.azzam.protectmyfiles.util.StorageUtils
import com.azzam.protectmyfiles.util.UIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

class HomeViewModel(private val repository: Repository) : ViewModel() {

    var isEncryptionRunning by mutableStateOf(false)
        private set

    var fileMetaData by mutableStateOf<FileMetadata?>(null)
        private set

    var image by mutableStateOf<Any?>(null)
        private set

    private val _showMessage = Channel<UIEvent>()
    val showMessage = _showMessage.receiveAsFlow()

    fun handleActivityResult(context: Context, data: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                data?.let { result ->
                    fileMetaData = StorageUtils.formFileMetadataFromData(context, result)

                    if (fileMetaData != null && fileMetaData?.fileUri != null) {

                        if (fileMetaData?.canBeEncrypted() != true) {
                            _showMessage.trySend(UIEvent.ShowStringResource(R.string.file_size_error))
                            fileMetaData = null
                            return@let
                        }

                        val file = File(
                            context.cacheDir,
                            fileMetaData?.fullFileName
                                ?: "${UUID.randomUUID()}${fileMetaData?.fileExt}"
                        )

                        file.outputStream().use { stream ->
                            context
                                .contentResolver
                                .openInputStream(fileMetaData?.fileUri!!)
                                ?.copyTo(stream)
                        }
                        val fileType = FileType.getFileType(fileMetaData?.fileExt)
                        fileMetaData?.fileType = fileType
                        when (fileType) {
                            is FileType.Image -> {
                                image = result
                            }

                            is FileType.Video -> {
                                val mMMR = MediaMetadataRetriever()
                                mMMR.setDataSource(context, result)
                                val videoImageFile = File(
                                    context.cacheDir,
                                    "${UUID.randomUUID()}$JPG_FORMAT"
                                )

                                videoImageFile.outputStream().use { stream ->
                                    val byteArrayOutputStream = ByteArrayOutputStream()
                                    mMMR.frameAtTime?.compress(
                                        Bitmap.CompressFormat.JPEG,
                                        50,
                                        byteArrayOutputStream
                                    )
                                    stream.write(byteArrayOutputStream.toByteArray())
                                }
                                image = videoImageFile.toUri()

                            }

                            else -> {
                                image = fileType.image
                            }
                        }
                        file.delete()
                    } else {
                        _showMessage.trySend(UIEvent.ShowStringResource(R.string.error_occurred))
                    }
                }
            } catch (e: Exception) {
                _showMessage.trySend(UIEvent.ShowStringMessage(e.message))
            }
        }
    }

    fun insertEncryptedDto(context: Context, fileMetaData: FileMetadata) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isEncryptionRunning = true

                val cryptoManager = CryptoManager()

                val file = File(
                    context.cacheDir,
                    fileMetaData.fullFileName ?: "${UUID.randomUUID()}${fileMetaData.fileExt}"
                )

                file.outputStream().use { stream ->
                    context
                        .contentResolver
                        .openInputStream(fileMetaData.fileUri!!)
                        ?.copyTo(stream)
                }
                val bytes = file.readBytes()
                Timber.v("bytes: ${bytes.size}")
                val tempData = cryptoManager.encrypt(bytes)
                Timber.v("enc bytes: ${tempData.data.size}")
                val encryptedModel = EncryptedModel(
                    name = fileMetaData.getFileName()!!,
                    fileSize = fileMetaData.fileSize,
                    fileType = fileMetaData.fileType!!,
                    iv = tempData.iv
                )

                file.delete()

                repository.insertEncryptedModel(encryptedModel, tempData.data).onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            _showMessage.trySend(UIEvent.ShowStringMessage(result.message))
                        }

                        is Resource.Loading -> {
                            isEncryptionRunning = true
                        }

                        is Resource.Success -> {
                            _showMessage.trySend(UIEvent.ShowStringResource(R.string.file_successfully_encrypted))
                            isEncryptionRunning = false
                            this@HomeViewModel.fileMetaData = null
                            image = null
                        }
                    }
                }.launchIn(this)

            } catch (e: Exception) {
                _showMessage.trySend(UIEvent.ShowStringResource(R.string.error_occurred))
            } finally {
                isEncryptionRunning = false
            }
        }
    }
}