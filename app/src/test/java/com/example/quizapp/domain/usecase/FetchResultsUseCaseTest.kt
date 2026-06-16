package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.results.Results
import com.example.quizapp.domain.repository.ResultsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FetchResultsUseCaseTest {

    private var repository = mockk<ResultsRepository>()
    private lateinit var useCase: FetchResultsUseCase

    @Before
    fun setup() {
        useCase = FetchResultsUseCase(repository)
    }

    @Test
    fun `invoke returns success`() = runBlocking {
        // --- ARRANGE ---
        val fakeData = listOf(
            Results(
                userId = "fakeUserId",
                category = "fakeCategory",
                correctAnswers = 1,
                incorrectAnswers = 0,
                username = "fakeUsername"
            )
        )
        coEvery { repository.fetchResults() } returns Result.success(fakeData)

        // --- ACT ---
        val result = useCase.invoke()

        // --- ASSERT ---
        assert(result.isSuccess)
        assert(result.getOrNull() == fakeData)
    }

    @Test
    fun `invoke returns failure`() = runBlocking {
        // --- ARRANGE ---
        val error = Exception("fake error")
        coEvery { repository.fetchResults() } returns Result.failure(error)

        // --- ACT ---
        val result = useCase.invoke()

        // --- ASSERT ---
        assert(result.isFailure)
    }
}