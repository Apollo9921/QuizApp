package com.example.quizapp.data.repository

import com.example.quizapp.BuildConfig
import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.data.network.instance.Instance
import com.example.quizapp.data.network.service.QuizService
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class QuizRepositoryImpl(private val instance: Instance) : QuizService {
    override suspend fun getQuiz(
        category: String,
        level: String,
        limit: Int
    ): List<QuizDTO> {
        return instance.httpClient.get {
            url("${BuildConfig.BASE_URL}questions")
            contentType(ContentType.Application.Json)
            parameter("limit", limit)
            parameter("categories", category)
            parameter("difficulties", level)
        }.body()
    }
}