package com.example.quizapp.domain.repository

import com.example.quizapp.data.network.dto.CloudQuizInputItem
import com.example.quizapp.data.network.dto.TranslatedQuizResult
import com.example.quizapp.domain.result.AppResult

interface CloudQuizTranslator {
    suspend fun translateQuizBlock(rawQuestions: List<CloudQuizInputItem>): AppResult<List<TranslatedQuizResult>>
}