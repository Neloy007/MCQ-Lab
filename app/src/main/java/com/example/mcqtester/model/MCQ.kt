package com.example.mcqtestlab.model


data class MCQ(
    val options: List<String> = listOf(),
    val question: String = "",
    val optionA: String = "",
    val optionB: String = "",
    val optionC: String = "",
    val optionD: String = "",
    val correctAnswer: String = ""
) {
    val correctAnswerIndex: Int
        get() = when (correctAnswer) {
            optionA -> 0
            optionB -> 1
            optionC -> 2
            optionD -> 3
            else -> -1
        }
}

