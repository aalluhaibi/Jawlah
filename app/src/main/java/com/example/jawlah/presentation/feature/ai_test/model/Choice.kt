package com.example.jawlah.presentation.feature.ai_test.model

data class Choice(
    val text: String,
    val isCorrect: Boolean
)

data class Question(
    val text: String,
    val choices: List<Choice>,
    val answered: Boolean
)