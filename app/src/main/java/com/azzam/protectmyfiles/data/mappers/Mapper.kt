package com.azzam.protectmyfiles.data.mappers

import com.azzam.protectmyfiles.data.local.EncryptedEntity
import com.azzam.protectmyfiles.data.local.PasswordEntity
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.domain.model.PasswordModel
import com.azzam.protectmyfiles.util.CryptoManager
import com.azzam.protectmyfiles.util.FileType

fun EncryptedEntity.toEncryptedModel(): EncryptedModel {
    return EncryptedModel(
        id = id,
        name = name,
        fileType = FileType.getFileType(extension),
        fileSize = fileSize,
        iv = iv
    )
}

fun EncryptedModel.toEncryptedEntity(): EncryptedEntity {
    return EncryptedEntity(
        id = id,
        name = name,
        fileSize = fileSize,
        extension = fileType.extension ?: "",
        iv = iv
    )
}

fun PasswordModel.toPasswordEntity(): PasswordEntity {
    val cryptoManager = CryptoManager()
    val passwordData = cryptoManager.encrypt(this.password.toByteArray())
    return PasswordEntity(
        id = id,
        name = name,
        username = username,
        password = passwordData.data,
        iv = passwordData.iv,
    )
}

fun PasswordEntity.toPasswordModel(): PasswordModel {
    val cryptoManager = CryptoManager()
    val passwordData = cryptoManager.decrypt(this.password, this.iv)
    return PasswordModel(
        id = id,
        name = name,
        username = username,
        password = String(passwordData),
        iv = iv,
    )
}