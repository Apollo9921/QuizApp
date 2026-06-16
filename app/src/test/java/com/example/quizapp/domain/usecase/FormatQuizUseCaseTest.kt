package com.example.quizapp.domain.usecase

import com.example.quizapp.data.network.dto.TranslatedQuizResult
import com.example.quizapp.domain.model.quiz.Question
import com.example.quizapp.domain.model.quiz.Quiz
import com.example.quizapp.domain.repository.CloudQuizTranslator
import com.example.quizapp.domain.result.AppError
import com.example.quizapp.domain.result.AppResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FormatQuizUseCaseTest {

    private val cloudQuizTranslator = mockk<CloudQuizTranslator>()
    private val formatQuizUseCase = FormatQuizUseCase(cloudQuizTranslator)

    @Test
    fun `invoke returns success`() = runBlocking {
        // --- ARRANGE ---
        val fakeData = listOf(
            Quiz(
                id = "1",
                question = Question("fakeQuestion"),
                correctAnswer = "fakeCorrectAnswer",
                incorrectAnswers = listOf("fakeIncorrectAnswer1", "fakeIncorrectAnswer2"),
                category = "fakeCategory",
                isNiche = false,
                regions = listOf(""),
                difficulty = "fakeDifficulty",
                type = "fakeType",
                tags = listOf("")
            )
        )

        val fakeTranslatedData = listOf(
            TranslatedQuizResult(
                question = "fakeQuestion",
                correctAnswer = "fakeCorrectAnswer",
                incorrectAnswers = listOf("fakeIncorrectAnswer1", "fakeIncorrectAnswer2"),
            )
        )
        coEvery { cloudQuizTranslator.translateQuizBlock(any()) } returns AppResult.Success(fakeTranslatedData)

        // --- ACT ---
        val result = formatQuizUseCase.invoke(fakeData)

        // --- ASSERT ---
        assert(result is AppResult.Success)
        assert((result as AppResult.Success).data == fakeTranslatedData)
    }

    @Test
    fun `invoke send empty values returns unknown error`() = runBlocking {
        // --- ARRANGE ---
        val fakeData = listOf(
            Quiz(
                id = "",
                question = Question(""),
                correctAnswer = "",
                incorrectAnswers = listOf("", ""),
                category = "",
                isNiche = false,
                regions = listOf(""),
                difficulty = "",
                type = "",
                tags = listOf("")
            )
        )
        coEvery { cloudQuizTranslator.translateQuizBlock(any()) } returns AppResult.Error(AppError.Unknown)

        // --- ACT ---
        val result = formatQuizUseCase.invoke(fakeData)

        // --- ASSERT ---
        assert(result is AppResult.Error)
    }
}