package com.apollo9921.quizrise.domain.repository

import com.apollo9921.quizrise.data.network.dto.CloudQuizInputItem
import com.apollo9921.quizrise.data.network.dto.TranslatedQuizResult
import com.apollo9921.quizrise.domain.result.AppResult

interface CloudQuizTranslator {
    suspend fun translateQuizBlock(rawQuestions: List<CloudQuizInputItem>): AppResult<List<TranslatedQuizResult>>
}