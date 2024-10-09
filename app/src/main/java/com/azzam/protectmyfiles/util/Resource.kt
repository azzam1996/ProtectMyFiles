package com.azzam.protectmyfiles.util

sealed class Resource<T>(val data: T? = null, val message: String? = null, val errorCode: Int? = null) {
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Success<T>(data: T?,message: String? = null): Resource<T>(data,message)
    class Error<T>(message: String?, data: T? = null, errorCode: Int? = null): Resource<T>(data, message, errorCode)
}