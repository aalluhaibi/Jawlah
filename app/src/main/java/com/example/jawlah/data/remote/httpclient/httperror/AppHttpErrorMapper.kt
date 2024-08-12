package com.example.jawlah.data.remote.httpclient.httperror

import android.content.Context
import com.example.jawlah.R
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException

class AppHttpErrorMapper(private val context: Context) : ErrorMapper {

    private val json by lazy {
        Json { ignoreUnknownKeys = true }
    }

    override suspend fun mapError(throwable: Throwable): String {
        return when (throwable) {
            is RedirectResponseException -> throwable.message
            is ClientRequestException -> {
                val errorContent = throwable.response.bodyAsText()
                val parsedError = json.decodeFromString<HttpErrorResponse>(
                    errorContent
                )
                parsedError.message ?: context.getString(R.string.unknown_error)
            }

            is ServerResponseException -> context.getString(R.string.server_exception)
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is ConnectTimeoutException -> context.getString(R.string.connection_timeout)
            is ConnectException -> context.getString(R.string.connection_error)
            else -> context.getString(R.string.unknown_error)
        }
    }
}