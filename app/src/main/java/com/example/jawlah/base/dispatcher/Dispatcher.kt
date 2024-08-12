package com.example.jawlah.base.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatcher {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}