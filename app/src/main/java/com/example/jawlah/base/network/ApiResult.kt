package com.example.jawlah.base.network

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class Error(val exception: Throwable, val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
    object Offline : ApiResult<Nothing>()
}
