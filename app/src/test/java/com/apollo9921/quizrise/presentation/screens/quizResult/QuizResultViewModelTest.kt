package com.apollo9921.quizrise.presentation.screens.quizResult

import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.CalculateQuizResultUseCase
import com.apollo9921.quizrise.domain.usecase.SaveQuizUseCase
import com.apollo9921.quizrise.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizResultViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val calculateQuizResultUseCase = mockk<CalculateQuizResultUseCase>()
    private val saveQuizUseCase = mockk<SaveQuizUseCase>()

    private lateinit var viewModel: QuizResultViewModel

    private fun initViewModel(category: String, correctAnswers: Int, incorrectAnswers: Int) {
        viewModel = QuizResultViewModel(
            calculateQuizResultUseCase,
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

        coEvery { calculateQuizResultUseCase.invoke(correctAnswers) } returns AppResult.Success(Pair(15, 20))
        coEvery { saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers) } returns AppResult.Success(Unit)

        // --- ACT ---
        initViewModel(category, correctAnswers, incorrectAnswers)
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is QuizResultViewModel.UIState.Success)
        assert((uiState as QuizResultViewModel.UIState.Success).pointsReceived == 15)
        assert(uiState.pointsToNextLevel == 20)
    }

    @Test
    fun `saveQuizProcess saved quiz process incorrectly calculateQuizResultUseCase`() = runTest {
        // --- ARRANGE ---
        val category = "History"
        val correctAnswers = 3
        val incorrectAnswers = 2

        coEvery { calculateQuizResultUseCase.invoke(correctAnswers) } returns AppResult.Error(AppError.Unknown)

        // --- ACT ---
        initViewModel(category, correctAnswers, incorrectAnswers)
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is QuizResultViewModel.UIState.Error)
    }

    @Test
    fun `saveQuizProcess saved quiz process incorrectly saveQuizUseCase`() = runTest {
        // --- ARRANGE ---
        val category = "History"
        val correctAnswers = 3
        val incorrectAnswers = 2

        coEvery { calculateQuizResultUseCase.invoke(correctAnswers) } returns AppResult.Success(Pair(15, 20))
        coEvery { saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers) } returns AppResult.Error(AppError.Unknown)

        // --- ACT ---
        initViewModel(category, correctAnswers, incorrectAnswers)
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is QuizResultViewModel.UIState.Error)
    }
}