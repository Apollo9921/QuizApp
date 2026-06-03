package com.example.quizapp.domain.usecase

import com.example.quizapp.data.network.dto.QuizDTO

class FormatQuizUseCase {
    operator fun invoke(result: List<QuizDTO>): ArrayList<String> {
        val data = result
        val answers: ArrayList<String> = ArrayList()
        for (i in 0 until data.size) {
            answers.add(data[i].correctAnswer)
            answers.addAll(data[i].incorrectAnswers)
        }
        return answers
    }
}