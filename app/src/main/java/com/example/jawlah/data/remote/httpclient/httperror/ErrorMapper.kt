package com.example.jawlah.data.remote.httpclient.httperror

interface ErrorMapper {
    suspend fun mapError(throwable: Throwable): String
}