package com.example.jawlah.presentation.feature.ask_gemi

import android.graphics.Bitmap
import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String = "",
    val img: Bitmap? = null,
    val participant: Participant
)
