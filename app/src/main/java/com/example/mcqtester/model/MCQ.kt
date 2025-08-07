package com.example.mcqtestlab.model

data class MCQ(
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = -1
)
