package com.example.quizapp.model.quiz.repository

import com.example.quizapp.BuildConfig
import com.example.quizapp.model.quiz.network.KtorClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class QuizRepository {

    suspend fun getQuiz(category: String, level: String): HttpResponse =
        KtorClient.httpClient.get {
            url("${BuildConfig.BASE_URL}questions?limit=5")
            contentType(ContentType.Application.Json)
            parameter("categories", category)
            parameter("difficulties", level)
        }
}