package com.apollo9921.quizrise.presentation.screens.quizResult

import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.SaveQuizUseCase
import com.apollo9921.quizrise.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizResultViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val saveQuizUseCase = mockk<SaveQuizUseCase>()

    private lateinit var viewModel: QuizResultViewModel

    private fun initViewModel(category: String, correctAnswers: Int, incorrectAnswers: Int) {
        viewModel = QuizResultViewModel(
            saveQuizUseCase,
            category,
            correctAnswers,
            incorrectAnswers
        )
    }

    @Test
    fun `saveQuizProcess saved quiz process correctly`() = runTest {
        // --- ARRANGE ---
        val category = "History"
        val correctAnswers = 3
        val incorrectAnswers = 2

        coEvery { saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers) } returns AppResult.Success(Pair(15, 135))

        // --- ACT ---
        initViewModel(category, correctAnswers, incorrectAnswers)
        advanceUntilIdle()

        // --- ASSERT ---
        assert(viewModel.total == 5)
        assert(viewModel.pointsReceived.intValue == 15)
        assert(viewModel.pointsToNextLevel.intValue == 135)
    }
}