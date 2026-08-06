package com.apollo9921.quizrise.domain.usecase

import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.PlayerLevel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CalculateQuizResultUseCaseTest {

    private val userRepository = mockk<UserRepository>()

    private val calculateQuizResultUseCase = CalculateQuizResultUseCase(userRepository)

    @Test
    fun `invoke returns success`() = runBlocking {
        // --- ARRANGE ---
        val correctAnswers = 3
        val fakeUser = User("", "FakeName", 50, 100, "Recruit")
        val currentLevel = PlayerLevel.getLevelByPoints(fakeUser.totalPoints)

        coEvery { userRepository.fetchUser() } returns Result.success(fakeUser)

        // --- ACT ---
        val result = calculateQuizResultUseCase.invoke(correctAnswers)

        // --- ASSERT ---
        assert(result is AppResult.Success)
        assert((result as AppResult.Success).data.first == correctAnswers * 5)
        assert(result.data.second == (currentLevel.maxPoints + 5) - (fakeUser.totalPoints + correctAnswers * 5))
    }

    @Test
    fun `invoke returns failure`() = runBlocking {
        // --- ARRANGE ---
        val correctAnswers = 3
        coEvery { userRepository.fetchUser() } returns Result.failure(Exception("Fake error"))

        // --- ACT ---
        val result = calculateQuizResultUseCase.invoke(correctAnswers)

        // --- ASSERT ---
        assert(result is AppResult.Error)
    }
}