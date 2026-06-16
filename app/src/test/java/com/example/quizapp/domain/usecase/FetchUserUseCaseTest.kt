package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.model.user.User
import com.example.quizapp.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FetchUserUseCaseTest {

    private val repository = mockk<UserRepository>()
    private lateinit var useCase: FetchUserUseCase

    @Before
    fun setup() {
        useCase = FetchUserUseCase(repository)
    }

    @Test
    fun `invoke returns success`() = runBlocking {
        // --- ARRANGE ---
        val fakeData = User(
            id = "fakeId",
            name = "fakeName",
            totalPoints = 1,
            totalPointsPossible = 1,
            badge = "fakeBadge"
        )
        coEvery { repository.fetchUser() } returns Result.success(fakeData)

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
        coEvery { repository.fetchUser() } returns Result.failure(error)

        // --- ACT ---
        val result = useCase.invoke()

        // --- ASSERT ---
        assert(result.isFailure)
    }
}