package com.example.jawlah.presentation.util

import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph(default = true)
annotation class AppNavGraph(
    val start: Boolean = false
)