package com.example.quizapp.data.network.service

import com.example.quizapp.BuildConfig
import com.example.quizapp.data.network.instance.Instance
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class Service {

    suspend fun getQuiz(category: String, level: String): HttpResponse =
        Instance.httpClient.get {
            url("${BuildConfig.BASE_URL}questions?limit=5")
            contentType(ContentType.Application.Json)
            parameter("categories", category)
            parameter("difficulties", level)
        }
}