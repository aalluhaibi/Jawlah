package com.example.jawlah.data.remote.httpclient.httperror

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HttpErrorResponse(
    val path: String? = null,
    @SerialName("date_time") val timestamp: String? = null,
    val message: String? = null
)
