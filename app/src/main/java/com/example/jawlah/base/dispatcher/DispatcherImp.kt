package com.example.jawlah.base.dispatcher

import kotlinx.coroutines.Dispatchers

class DispatcherImp : Dispatcher {
    override fun io() = Dispatchers.IO
    override fun ui() = Dispatchers.Main
    override fun default() = Dispatchers.Default
}