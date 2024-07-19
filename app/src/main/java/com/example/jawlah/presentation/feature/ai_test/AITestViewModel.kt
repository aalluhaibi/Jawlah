package com.example.jawlah.presentation.feature.ai_test

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.viewModelScope
import com.example.jawlah.BuildConfig
import com.example.jawlah.presentation.feature.ai_test.model.Question
import com.example.jawlah.presentation.util.BaseMviViewModel
import com.example.jawlah.presentation.util.parseQuestions
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AITestViewModel @Inject constructor() :
    BaseMviViewModel<AITestContract.Event, AITestContract.State, AITestContract.Effect>() {
    override fun setInitialState(): AITestContract.State = AITestContract.State()

    override fun handleEvents(event: AITestContract.Event) {
        when (event) {
            is AITestContract.Event.ChoiceSelected -> {
                setState {
                    copy(
                        questions = questions.map { question ->
                            if (event.question.text.trim()
                                    .toLowerCase(Locale.current) == question.text.trim()
                                    .toLowerCase(Locale.current)
                            ) {
                                question.copy(answered = true)
                            } else {
                                question
                            }
                        }.toMutableList(),
                        answerChanged = !answerChanged
                    )
                }
            }

            AITestContract.Event.NextQuestion -> {
                nextQuestion()
            }

            AITestContract.Event.PreviousQuestion -> {
                previousQuestion()
            }

            AITestContract.Event.ShufflesQuestions -> TODO()
            AITestContract.Event.LoadQuestions -> retrieveSuggestedPlaces("Ask me 10 multi-choices questions about Milan each question has two choices the first choice is correct. I want to separate each question by new line and each choice by comma. I expect the line to be in the following format: question,choice1,choice2")
        }
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", //gemini-pro-vision
        apiKey = BuildConfig.apiKey,
        generationConfig = generationConfig {
            temperature = 0.7f
        },
        systemInstruction = content {
            text("You are a capable travel assistant named Gemi.")
        }
    )

    private fun previousQuestion() {
        setState {
            copy(currentQuestionIndex = (currentQuestionIndex - 1).takeIf { it >= 0 } ?: 0)
        }
    }

    private fun nextQuestion() {
        setState {
            copy(currentQuestionIndex = (currentQuestionIndex + 1).takeIf { it < questions.size }
                ?: (questions.size - 1))
        }
    }

    private fun retrieveSuggestedPlaces(
        prompt: String
    ) {
        setState {
            copy(loading = true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    val questions = shuffleChoices(parseQuestions(outputContent))
                    setState {
                        copy(loading = false, questions = questions.toMutableList())
                    }
                }
            } catch (e: Exception) {
                setEffect { AITestContract.Effect.Error(e.localizedMessage ?: "") }
            }
        }
    }

    fun shuffleChoices(questions: List<Question>): List<Question> {
        return questions.map { question ->
            Question(question.text, question.choices.shuffled().toMutableList(), false)
        }
    }
}