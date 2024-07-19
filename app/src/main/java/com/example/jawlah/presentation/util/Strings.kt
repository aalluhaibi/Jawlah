package com.example.jawlah.presentation.util

import com.example.jawlah.presentation.feature.ai_test.model.Choice
import com.example.jawlah.presentation.feature.ai_test.model.Question

fun parseQuestions(questions: String): List<Question> {
    val result = mutableListOf<Question>()
    val questionBlocks = questions.split("\n\n")

    for (block in questionBlocks) {
        val lines = block.lines()
        if (lines.size >= 2) {
            val questionText = lines[0].trim()
            val choicesText = lines[1] // Get the line with choices
            val choices = choicesText.split(",").mapIndexed { idx, item ->
                if (idx == 0)
                    Choice(item.trim(), true)
                else
                    Choice(item.trim(), false)
            }
            val question = Question(questionText, choices, false)
            result.add(question)
        }
    }
    return result
}