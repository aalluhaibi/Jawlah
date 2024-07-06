package com.example.jawlah.data.remote.httpclient.httperror

import io.ktor.client.request.HttpRequestBuilder

class AppOfflineException(val msg: String, val request: HttpRequestBuilder) :
    Exception(msg)