package com.example.jawlah.presentation.feature.ai_test

import androidx.compose.runtime.mutableStateListOf
import com.example.jawlah.presentation.feature.ai_test.model.Choice
import com.example.jawlah.presentation.feature.ai_test.model.Question
import com.example.jawlah.presentation.util.ViewEvent
import com.example.jawlah.presentation.util.ViewSideEffect
import com.example.jawlah.presentation.util.ViewState

class AITestContract {
    sealed class Event : ViewEvent {
        data object NextQuestion : Event()
        data object PreviousQuestion : Event()
        data object ShufflesQuestions : Event()
        data object LoadQuestions : Event()
        data class ChoiceSelected(val choice: Choice, val question: Question) : Event()
    }

    data class State(
        val loading: Boolean = false,
        val questions: MutableList<Question> = mutableStateListOf(),
        val currentQuestionIndex: Int = 0,
        val answerChanged: Boolean = false
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