package com.azzam.protectmyfiles.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


object StorageUtils {
    private fun obtainFileInfoNameByUri(uri: Uri?, context: Context?): FileMetadata {
        var fullFileName: String?
        var fileMetadata = FileMetadata()

        when (uri?.scheme) {
            ContentResolver.SCHEME_CONTENT -> {
                var cursor: Cursor? = null
                var fileSize: Int? = null
                var fileName: String? = null
                var fileExtension: String? = null

                try {
                    cursor = context?.contentResolver?.query(uri, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val displayColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeColumnIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                        fullFileName = cursor.getString(displayColumnIndex)
                        fileSize = cursor.getInt(sizeColumnIndex)
                        fileName = if (fullFileName != null && fullFileName.indexOf(".") != -1) {
                            fullFileName
                        } else "temp_${Date().time}"
                        if(fullFileName?.contains(".") == true){
                            fileExtension = fullFileName.substring(fullFileName.lastIndexOf("."))
                        }
                    }
                } finally {
                    cursor?.close()
                }
                fileMetadata = FileMetadata(
                    fileSize = fileSize,
                    fullFileName = fileName,
                    fileExt = fileExtension,
                    fileUri = uri
                    )
            }

            ContentResolver.SCHEME_FILE -> fileMetadata = obtainOptimizedFileMetadata(uri, context)
        }
        return fileMetadata
    }

    private fun obtainOptimizedFileMetadata(imageUri: Uri, context: Context?): FileMetadata {
        val file = File(imageUri.path!!)
        val fileMetadata = FileMetadata()

        fileMetadata.fileSize = file.length().toInt()
        fileMetadata.fullFileName = file.name
        fileMetadata.fileExt = file.extension
        fileMetadata.fileUri = imageUri

        val fileEncodedBytes: ByteArray

        if (!fileMetadata.canBeEncrypted()) {
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream)
            fileEncodedBytes = stream.toByteArray()

            context!!.openFileOutput(fileMetadata.getFileName(), Context.MODE_PRIVATE)
                .use { outputStream ->
                    outputStream.write(fileEncodedBytes)
                    val uri = context
                        .getFileStreamPath(fileMetadata.getFileName())
                        .toUri()
                    fileMetadata.fileUri = uri
                    fileMetadata.fileSize = File(uri.path).length().toInt()
                }
        }
        return fileMetadata
    }


    /**
     * Form FileMetadata from file name and uri
     */
    fun formFileMetadataFromData(
        context: Context?,
        uri: Uri
    ): FileMetadata? {
        val fileMetadata = obtainFileInfoNameByUri(uri, context)
        return fileMetadata
    }

}

