package com.example.jawlah.presentation.feature.ask_gemi

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import com.example.jawlah.presentation.util.ViewEvent
import com.example.jawlah.presentation.util.ViewSideEffect
import com.example.jawlah.presentation.util.ViewState

class AskGemiContract {
    sealed class Event : ViewEvent {
        data object ShowImagePicker : Event()
        data class SubmitGeneralMessage(val message: Message?) : Event()
        data class SubmitLuggageClassificationMessage(val message: Message) : Event()
        data class SubmitLandmarkLensMessage(val message: Message) : Event()
        data class OnTextMessageChanged(val text: String) : Event()
        data class OnImageSelected(val image: Bitmap) : Event()
    }

    data class State(
        val loading: Boolean = false,
        val displayImagePicker: Boolean = false,
        val displaySelectedImage: Bitmap? = null,
        val textMessage: String = "",
        val message: Message? = null,
        val messages: MutableList<Message> = mutableStateListOf()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Error(
            val message: String,
            val action: (() -> Unit)? = null
        ) : Effect()


        sealed class Navigation : Effect() {
            data object Back : Navigation()
        }
    }
}