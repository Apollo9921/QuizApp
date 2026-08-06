package com.apollo9921.quizrise.domain.usecase

import android.content.Context
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test


class SaveQuizUseCaseTest {

    private val resultsRepository = mockk<ResultsRepository>()
    private val userRepository = mockk<UserRepository>()
    private val firebaseAuth = mockk<FirebaseAuth>()
    private val context = mockk<Context>(relaxed = true)

    private val saveQuizUseCase = SaveQuizUseCase(context, resultsRepository, userRepository, firebaseAuth)

    @Test
    fun `invoke saves quiz correctly`() = runBlocking {
        // --- ARRANGE ---
        val category = "Music"
        val correctAnswers = 3
        val incorrectAnswers = 2
        val badge = PlayerLevel.getLevelByPoints(0).badgeName

        val fakeUser = User("", "FakeName", 0, 0, badge)

        coEvery { userRepository.fetchUser() } returns Result.success(fakeUser)
        coEvery { resultsRepository.updateResults(any(), any(), any()) } returns Unit
        coEvery { resultsRepository.updatePoints(any(), any(), any()) } returns Unit
        coEvery { userRepository.updateBadge(any(), any()) } returns Unit
        coEvery { firebaseAuth.currentUser } returns null
        coEvery { userRepository.updateUserAndResults(any(), any()) } returns AppResult.Success(Unit)

        // --- ACT ---
        val result = saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers)

        // --- ASSERT ---
        assert(result is AppResult.Success)
    }

    @Test
    fun `invoke saves quiz incorrectly`() = runBlocking {
        // --- ARRANGE ---
        val category = "Music"
        val correctAnswers = 3
        val incorrectAnswers = 2
        val badge = PlayerLevel.getLevelByPoints(145).badgeName

        val fakeUser = User("", "FakeName", 145, 200, badge)

        coEvery { userRepository.fetchUser() } returns Result.success(fakeUser)
        coEvery { resultsRepository.updateResults(any(), any(), any()) } returns Unit
        coEvery { resultsRepository.updatePoints(any(), any(), any()) } returns Unit
        coEvery { userRepository.updateBadge(any(), any()) } returns Unit
        coEvery { firebaseAuth.currentUser } returns null
        coEvery { userRepository.updateUserAndResults(any(), any()) } returns AppResult.Error(AppError.Unknown)

        // --- ACT ---
        val result = saveQuizUseCase.invoke(category, correctAnswers, incorrectAnswers)

        // --- ASSERT ---
        assert(result is AppResult.Error)
    }

}