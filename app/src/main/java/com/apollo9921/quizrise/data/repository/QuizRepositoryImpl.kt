package com.apollo9921.quizrise.data.repository

import com.apollo9921.quizrise.BuildConfig
import com.apollo9921.quizrise.data.mapper.toQuiz
import com.apollo9921.quizrise.data.mapper.toSession
import com.apollo9921.quizrise.data.network.dto.QuizDTO
import com.apollo9921.quizrise.data.network.dto.SessionDTO
import com.apollo9921.quizrise.data.network.instance.Instance
import com.apollo9921.quizrise.domain.model.quiz.Quiz
import com.apollo9921.quizrise.domain.model.session.Session
import com.apollo9921.quizrise.domain.repository.QuizRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.io.IOException

class QuizRepositoryImpl(private val instance: Instance) : QuizRepository {
    override suspend fun getQuiz(
        category: String,
        level: String,
        session: String,
        limit: Int
    ): AppResult<List<Quiz>> {
        return try {
            val response: List<QuizDTO> = instance.httpClient.get {
                url("${BuildConfig.BASE_URL}questions")
                header("x-api-key", BuildConfig.TRIVIA_API_KEY)
                contentType(ContentType.Application.Json)
                parameter("limit", limit)
                parameter("categories", category)
                parameter("difficulties", level)
                parameter("session", session)
            }.body()

            AppResult.Success(response.map { it.toQuiz() })

        } catch (_: HttpRequestTimeoutException) {
            AppResult.Error(AppError.Timeout)
        } catch (_: ConnectTimeoutException) {
            AppResult.Error(AppError.NoInternetConnection)
        } catch (_: IOException) {
            AppResult.Error(AppError.Network)
        } catch (_: RedirectResponseException) {
            AppResult.Error(AppError.Server)
        } catch (_: ClientRequestException) {
            AppResult.Error(AppError.BadRequest)
        } catch (_: ServerResponseException) {
            AppResult.Error(AppError.ServerDown)
        } catch (_: Exception) {
            AppResult.Error(AppError.Unknown)
        }
    }

    override suspend fun createSession(): AppResult<Session> {
        return try {
            val response: SessionDTO = instance.httpClient.post {
                url("${BuildConfig.BASE_URL}session")
                header("x-api-key", BuildConfig.TRIVIA_API_KEY)
            }.body()

            AppResult.Success(response.toSession())

        } catch (_: HttpRequestTimeoutException) {
            AppResult.Error(AppError.Timeout)
        } catch (_: ConnectTimeoutException) {
            AppResult.Error(AppError.NoInternetConnection)
        } catch (_: IOException) {
            AppResult.Error(AppError.Network)
        } catch (_: RedirectResponseException) {
            AppResult.Error(AppError.Server)
        } catch (_: ClientRequestException) {
            AppResult.Error(AppError.BadRequest)
        } catch (_: ServerResponseException) {
            AppResult.Error(AppError.ServerDown)
        } catch (_: Exception) {
            AppResult.Error(AppError.Unknown)
        }
    }
}