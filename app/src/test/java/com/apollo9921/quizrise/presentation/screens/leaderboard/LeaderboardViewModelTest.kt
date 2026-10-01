package com.apollo9921.quizrise.presentation.screens.leaderboard

import com.apollo9921.quizrise.domain.model.results.Results
import com.apollo9921.quizrise.domain.model.user.User
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.GetTopPlayersByCategoryUseCase
import com.apollo9921.quizrise.domain.usecase.GetTopPlayersByLevelUseCase
import com.apollo9921.quizrise.domain.util.PlayerLevel
import com.apollo9921.quizrise.utils.MainDispatcherRule
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val firebaseAuth = mockk<FirebaseAuth>()
    private val getTopPlayersByLevelUseCase = mockk<GetTopPlayersByLevelUseCase>()
    private val getTopPlayersByCategoryUseCase = mockk<GetTopPlayersByCategoryUseCase>()
    private val fetchUserUseCase = mockk<FetchUserUseCase>()

    private lateinit var viewModel: LeaderboardViewModel

    private val fakeUser = User(
        id = "1",
        name = "John Doe",
        totalPoints = 100,
        totalPointsPossible = 150,
        badge = PlayerLevel.getLevelByPoints(100).badgeName,
        session = "session123"
    )

    private val fakeResults = listOf(
        Results(
            userId = "1",
            category = "History",
            correctAnswers = 10,
            incorrectAnswers = 5,
            username = "John Doe"
        )
    )

    private fun initViewModel() {
        viewModel = LeaderboardViewModel(
            firebaseAuth,
            getTopPlayersByLevelUseCase,
            getTopPlayersByCategoryUseCase,
            fetchUserUseCase
        )
    }

    @Test
    fun `getTopPlayersByLevel return success`() = runTest {
        // --- ARRANGE ---
        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>()
        coEvery { mockFirebaseUser.uid } returns "fake_uid"
        coEvery { firebaseAuth.currentUser } returns mockFirebaseUser
        coEvery { fetchUserUseCase.invoke() } returns Result.success(fakeUser)
        coEvery {
            getTopPlayersByLevelUseCase.invoke(any(), any(), any())
        } answers {
            val onSuccess = secondArg<(List<User>) -> Unit>()
            onSuccess.invoke(listOf(fakeUser))
        }

        // --- ACT ---
        initViewModel()
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is LeaderboardViewModel.UIState.Success)
    }

    @Test
    fun `getTopPlayersByLevel return failure`() = runTest {
        // --- ARRANGE ---
        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>()
        coEvery { mockFirebaseUser.uid } returns "fake_uid"
        coEvery { firebaseAuth.currentUser } returns mockFirebaseUser
        coEvery { fetchUserUseCase.invoke() } returns Result.success(fakeUser)
        coEvery {
            getTopPlayersByLevelUseCase.invoke(any(), any(), any())
        } answers {
            val onFailure = thirdArg<(Exception) -> Unit>()
            onFailure.invoke(HttpRequestTimeoutException("url", 1000L))
        }

        // --- ACT ---
        initViewModel()
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is LeaderboardViewModel.UIState.Error)
    }

    @Test
    fun `getTopPlayersByCategory return success`() = runTest {
        // --- ARRANGE ---
        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>()
        coEvery { mockFirebaseUser.uid } returns "fake_uid"
        coEvery { firebaseAuth.currentUser } returns mockFirebaseUser
        coEvery { fetchUserUseCase.invoke() } returns Result.success(fakeUser)
        coEvery {
            getTopPlayersByLevelUseCase.invoke(any(), any(), any())
        } answers {
            val onSuccess = secondArg<(List<User>) -> Unit>()
            onSuccess.invoke(listOf(fakeUser))
        }
        coEvery {
            getTopPlayersByCategoryUseCase.invoke(any(), any(), any())
        } answers {
            val onSuccess = secondArg<(List<Results>) -> Unit>()
            onSuccess.invoke(fakeResults)
        }

        // --- ACT ---
        initViewModel()
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is LeaderboardViewModel.UIState.Success)
    }

    @Test
    fun `getTopPlayersByCategory return failure`() = runTest {
        // --- ARRANGE ---
        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>()
        coEvery { mockFirebaseUser.uid } returns "fake_uid"
        coEvery { firebaseAuth.currentUser } returns mockFirebaseUser
        coEvery { fetchUserUseCase.invoke() } returns Result.success(fakeUser)
        coEvery {
            getTopPlayersByLevelUseCase.invoke(any(), any(), any())
        } answers {
            val onFailure = thirdArg<(Exception) -> Unit>()
            onFailure.invoke(HttpRequestTimeoutException("url", 1000L))
        }
        coEvery {
            getTopPlayersByCategoryUseCase.invoke(any(), any(), any())
        } answers {
            val onFailure = thirdArg<(Exception) -> Unit>()
            onFailure.invoke(HttpRequestTimeoutException("url", 1000L))
        }

        // --- ACT ---
        initViewModel()
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is LeaderboardViewModel.UIState.Error)
    }

    @Test
    fun `fetchUser return failure`() = runTest {
        // --- ARRANGE ---
        val mockFirebaseUser = mockk<com.google.firebase.auth.FirebaseUser>()
        coEvery { mockFirebaseUser.uid } returns "fake_uid"
        coEvery { firebaseAuth.currentUser } returns mockFirebaseUser
        coEvery { fetchUserUseCase.invoke() } returns Result.failure(Exception())

        // --- ACT ---
        initViewModel()
        advanceUntilIdle()

        // --- ASSERT ---
        val uiState = viewModel.uiState.value
        assert(uiState is LeaderboardViewModel.UIState.Error)
    }
}