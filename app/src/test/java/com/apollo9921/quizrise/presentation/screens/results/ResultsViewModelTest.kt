package com.apollo9921.quizrise.presentation.screens.results

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.usecase.FetchResultsUseCase
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.domain.util.QuizCategory
import com.apollo9921.quizrise.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ResultsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val fetchUserUseCase = mockk<FetchUserUseCase>()
    private val fetchResultsUseCase = mockk<FetchResultsUseCase>()
    private val viewModel = ResultsViewModel(fetchUserUseCase, fetchResultsUseCase)

    val fakeUser = User(
        id = "1",
        name = "John Doe",
        totalPoints = 100,
        totalPointsPossible = 100,
        badge = PlayerLevel.RECRUIT.badgeName,
        session = "123"
    )

    val fakeResult = Results(
        userId = "1",
        category = QuizCategory.ARTS_AND_LITERATURE.categoryName,
        correctAnswers = 10,
        incorrectAnswers = 0,
        username = "John Doe"
    )

    @Test
    fun `fetchUserAndResults returns success`() = runBlocking {
        // --- ARRANGE ---
        coEvery { fetchUserUseCase.invoke() } returns Result.success(fakeUser)
        coEvery { fetchResultsUseCase.invoke() } returns Result.success(listOf(fakeResult))

        // --- ACT ---
        viewModel.fetchUserAndResults()

        // --- ASSERT ---
        val state = viewModel.uiState.value
        assert(state is ResultsViewModel.UIState.Success)
        assert((state as ResultsViewModel.UIState.Success).user == fakeUser)
        assert(state.results == listOf(fakeResult))
    }

    @Test
    fun `fetchUserAndResults returns user null`() = runBlocking {
        // --- ARRANGE ---
        coEvery { fetchUserUseCase.invoke() } returns Result.success(User())
        coEvery { fetchResultsUseCase.invoke() } returns Result.success(listOf(fakeResult))

        // --- ACT ---
        viewModel.fetchUserAndResults()

        // --- ASSERT ---
        val state = viewModel.uiState.value
        assert(state is ResultsViewModel.UIState.Success)
        assert((state as ResultsViewModel.UIState.Success).user == User())
        assert(state.results == listOf(fakeResult))
    }

    @Test
    fun `fetchUserAndResults returns results null`() = runBlocking {
        // --- ARRANGE ---
        coEvery { fetchUserUseCase.invoke() } returns Result.success(fakeUser)
        coEvery { fetchResultsUseCase.invoke() } returns Result.success(emptyList())

        // --- ACT ---
        viewModel.fetchUserAndResults()

        // --- ASSERT ---
        val state = viewModel.uiState.value
        assert(state is ResultsViewModel.UIState.Error)
    }

    @Test
    fun `fetchUserAndResults returns failure`() = runBlocking {
        // --- ARRANGE ---
        coEvery { fetchUserUseCase.invoke() } returns Result.failure(Exception())
        coEvery { fetchResultsUseCase.invoke() } returns Result.failure(Exception())

        // --- ACT ---
        viewModel.fetchUserAndResults()

        // --- ASSERT ---
        val state = viewModel.uiState.value
        assert(state is ResultsViewModel.UIState.Error)
    }
}