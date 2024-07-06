package com.example.jawlah.data.remote.httpclient.httperror

import io.ktor.client.request.HttpRequest

class AppHttpException(val msg: String, val request: HttpRequest, val throwable: Throwable) :
    Exception(msg)