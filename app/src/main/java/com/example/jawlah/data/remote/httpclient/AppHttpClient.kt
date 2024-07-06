package com.example.jawlah.data.remote.httpclient

import android.util.Log
import com.example.jawlah.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.example.jawlah.data.remote.httpclient.httperror.AppHttpException
import com.example.jawlah.data.remote.httpclient.httperror.ErrorMapper
import javax.inject.Inject

class AppHttpClient @Inject constructor(
    private val engine: HttpClientEngineFactory<*>,
    private val errorMapper: ErrorMapper
) {

    private val client by lazy {
        HttpClient(engine) {
            expectSuccess = true
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, request: HttpRequest ->
                    if (BuildConfig.DEBUG) {
                        Log.e("handleResponseExceptionWithRequest", "Error -> $exception")
                    }
                    val clientException = exception as? ClientRequestException

                    val message = errorMapper.mapError(exception)
                    throw AppHttpException(message, request, exception)
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        allowSpecialFloatingPointValues = true
                        useArrayPolymorphism = true
                        coerceInputValues = true
                    }
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        if (BuildConfig.DEBUG) {
                            Log.v("Response", "response $message")
                        }
                    }
                }
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                // Timeout for the whole request execution sequence, including redirects.
                requestTimeoutMillis = 30000 // 30 seconds

                // Timeout for connecting to the remote host.
                connectTimeoutMillis = 30000 // 30 seconds

                // Timeout for reading the response from the remote host.
                socketTimeoutMillis = 30000 // 30 seconds
            }
            install(DefaultRequest) {
                header(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json
                )


                url {
                    protocol = URLProtocol.HTTPS
                    //host = BuildConfig.SERVER_URL
                }
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                execute(request)
            }
        }
    }

    operator fun invoke(): HttpClient = client
}