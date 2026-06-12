package com.example.quizapp.domain.repository

import com.example.quizapp.data.network.dto.CloudQuizInputItem
import com.example.quizapp.data.network.dto.TranslatedQuizResult

interface CloudQuizTranslator {
    suspend fun translateQuizBlock(rawQuestions: List<CloudQuizInputItem>): List<TranslatedQuizResult>
}