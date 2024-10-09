package com.azzam.protectmyfiles.util

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.azzam.protectmyfiles.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class FileType(
    @DrawableRes val image: Int,
    @StringRes val type: Int,
    open val extension: String? = null
) : Parcelable {
    class Image(override val extension: String?) : FileType(R.drawable.ic_type_image, R.string.image)
    class Video(override val extension: String?) : FileType(R.drawable.ic_type_video, R.string.video)
    class Audio(override val extension: String?) : FileType(R.drawable.ic_type_audio, R.string.audio)
    class Word(override val extension: String?) : FileType(R.drawable.ic_type_word, R.string.word)
    class Pdf(override val extension: String?) : FileType(R.drawable.ic_type_pdf, R.string.pdf)
    class PowerPoint(override val extension: String?) : FileType(R.drawable.ic_type_ppt, R.string.power_point)
    class Excel(override val extension: String?) : FileType(R.drawable.ic_type_excel, R.string.excel)
    class Document(override val extension: String?) : FileType(R.drawable.ic_type_document, R.string.document)

    companion object {
        fun getFileType(extension: String?): FileType {
            return when (extension?.lowercase()) {
                // Image file types
                ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".svg" -> Image(extension = extension)

                // Video file types
                ".mp4", ".mkv", ".webm", ".avi", ".flv", ".mov", ".wmv", ".ts", ".3gp" -> Video(extension = extension)

                // Audio file types
                ".mp3", ".wav", ".aac", ".flac", ".ogg", ".m4a", ".wma" -> Audio(extension = extension)

                // Document file types
                ".pdf" -> Pdf(extension = extension)
                ".doc", ".docx" -> Word(extension = extension)
                ".ppt", ".pptx" -> PowerPoint(extension = extension)
                ".xls", ".xlsx" -> Excel(extension = extension)
                else -> Document(extension = extension)
            }
        }
    }

}