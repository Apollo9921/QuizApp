package com.example.quizapp.data.repository

import com.example.quizapp.BuildConfig
import com.example.quizapp.data.network.dto.QuizDTO
import com.example.quizapp.data.network.instance.Instance
import com.example.quizapp.domain.repository.QuizRepository
import com.example.quizapp.domain.result.AppResult
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.io.IOException

class QuizRepositoryImpl(private val instance: Instance) : QuizRepository {
    override suspend fun getQuiz(
        category: String,
        level: String,
        limit: Int
    ): AppResult<List<QuizDTO>> {
        return try {
            val response: List<QuizDTO> = instance.httpClient.get {
                url("${BuildConfig.BASE_URL}questions")
                contentType(ContentType.Application.Json)
                parameter("limit", limit)
                parameter("categories", category)
                parameter("difficulties", level)
            }.body()

            AppResult.Success(response)

        } catch (_: HttpRequestTimeoutException) {
            AppResult.Error("Request timed out. Please try again.")
        } catch (_: ConnectTimeoutException) {
            AppResult.Error("Check your internet connection.")
        } catch (_: IOException) {
            AppResult.Error("Network error occurred. Are you offline?")
        } catch (_: RedirectResponseException) {
            AppResult.Error("Server redirect error.")
        } catch (_: ClientRequestException) {
            AppResult.Error("Invalid request")
        } catch (_: ServerResponseException) {
            AppResult.Error("Server is currently down.")
        } catch (_: Exception) {
            AppResult.Error("An unexpected error occurred")
        }
    }
}