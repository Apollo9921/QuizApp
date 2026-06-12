package com.example.quizapp.data.repository

import androidx.appcompat.app.AppCompatDelegate
import com.example.quizapp.data.network.dto.CloudQuizInputItem
import com.example.quizapp.data.network.dto.TranslatedQuizResult
import com.example.quizapp.domain.repository.CloudQuizTranslator
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class CloudQuizTranslatorImpl(
    private val functions: FirebaseFunctions
): CloudQuizTranslator {

    override suspend fun translateQuizBlock(
        rawQuestions: List<CloudQuizInputItem>
    ): List<TranslatedQuizResult> = withContext(Dispatchers.IO) {

        val currentLanguage = AppCompatDelegate.getApplicationLocales()[0]?.language
            ?: Locale.getDefault().language

        if (currentLanguage != "pt" && currentLanguage != "es") {
            return@withContext rawQuestions.map {
                TranslatedQuizResult(
                    question = it.question,
                    correctAnswer = it.correctAnswer,
                    incorrectAnswers = it.incorrectAnswers
                )
            }
        }

        val data = hashMapOf(
            "questions" to rawQuestions.map {
                mapOf(
                    "id" to it.id,
                    "question" to it.question,
                    "correctAnswer" to it.correctAnswer,
                    "incorrectAnswers" to it.incorrectAnswers
                )
            },
            "language" to currentLanguage
        )

        try {
            val result = functions
                .getHttpsCallable("getTranslatedQuiz")
                .call(data)
                .await()

            val resultMap = result.getData() as? Map<*, *>
            val resultsList = resultMap?.get("results") as? List<*>

            val translatedQuestions = resultsList?.mapNotNull { item ->
                val itemMap = item as? Map<*, *>

                val question = itemMap?.get("question") as? String
                val correctAnswer = itemMap?.get("correctAnswer") as? String
                @Suppress("UNCHECKED_CAST")
                val incorrectAnswers = itemMap?.get("incorrectAnswers") as? List<String>

                if (question != null && correctAnswer != null && incorrectAnswers != null) {
                    TranslatedQuizResult(
                        question = question,
                        correctAnswer = correctAnswer,
                        incorrectAnswers = incorrectAnswers
                    )
                } else {
                    null
                }
            }

            return@withContext translatedQuestions ?: rawQuestions.map {
                TranslatedQuizResult(it.question, it.correctAnswer, it.incorrectAnswers)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext rawQuestions.map {
                TranslatedQuizResult(it.question, it.correctAnswer, it.incorrectAnswers)
            }
        }
    }
}