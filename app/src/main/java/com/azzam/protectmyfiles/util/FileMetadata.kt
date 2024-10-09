package com.azzam.protectmyfiles.util

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FileMetadata(
    var fullFileName: String? = null,
    var fileSize: Int? = null,
    var fileUrl: String? = null,
    var fileUri: Uri? = null,
    var fileExt: String? = null,
    var fileType: FileType? = null,
    var id: Int? = null
) : Parcelable {
    fun getFileName(): String? {
        val index = fullFileName?.lastIndexOf(".")
        return if (index != null && index >= 0) {
            fullFileName?.substring(0, index)
        } else {
            fullFileName
        }
    }


    fun canBeEncrypted(): Boolean = (fileSize ?: 0) < MAX_CONTENT_SIZE


    @SuppressLint("DefaultLocale")
    fun fileSize() = String.format("%3.2f MB.", (fileSize ?: 0) / (1024f * 1024f))

    companion object {
        const val MAX_CONTENT_SIZE = 52428800L //max file size 50 MB
    }

}