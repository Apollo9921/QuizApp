package com.apollo9921.quizrise.presentation.screens.register

import com.apollo9921.quizrise.R
import com.apollo9921.quizrise.domain.result.AppError
import com.apollo9921.quizrise.domain.result.AppResult
import com.apollo9921.quizrise.domain.usecase.PostUserUseCase
import com.apollo9921.quizrise.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private var postUserUseCase = mockk<PostUserUseCase>()
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        viewModel = RegisterViewModel(postUserUseCase)
    }

    @Test
    fun `onRegisterClick returns success`() = runBlocking {
        // --- ARRANGE ---
        val email = "test@example.com"
        val password = "password"
        val confirmPassword = "password"
        val onNavigateBack = {}

        coEvery { postUserUseCase.invoke(email, password, confirmPassword) } returns AppResult.Success(Unit)

        // --- ACT ---
        viewModel.onRegisterClick(email, password, confirmPassword, onNavigateBack)

        // --- ASSERT ---
        assert(viewModel.uiState.value is RegisterViewModel.UIState.Idle)
    }

    @Test
    fun `onRegisterClick return Empty Fields Error`() = runBlocking {
        // --- ARRANGE ---
        val email = ""
        val password = ""
        val confirmPassword = ""
        val onNavigateBack = {}

        coEvery { postUserUseCase.invoke(email, password, confirmPassword) } returns AppResult.Error(AppError.EmptyFields)

        // --- ACT ---
        viewModel.onRegisterClick(email, password, confirmPassword, onNavigateBack)

        // --- ASSERT ---
        assert(viewModel.uiState.value is RegisterViewModel.UIState.Error)
        assert((viewModel.uiState.value as RegisterViewModel.UIState.Error).message == R.string.empty_fields)
    }

    @Test
    fun `onRegisterClick return Invalid Email Format Error`() = runBlocking {
        // --- ARRANGE ---
        val email = "test@"
        val password = "password"
        val confirmPassword = "password"
        val onNavigateBack = {}

        coEvery { postUserUseCase.invoke(email, password, confirmPassword) } returns AppResult.Error(AppError.InvalidEmailFormat)

        // --- ACT ---
        viewModel.onRegisterClick(email, password, confirmPassword, onNavigateBack)

        // --- ASSERT ---
        assert(viewModel.uiState.value is RegisterViewModel.UIState.Error)
        assert((viewModel.uiState.value as RegisterViewModel.UIState.Error).message == R.string.invalid_email_format)
    }

    @Test
    fun `onRegisterClick return Password Length Error`() = runBlocking {
        // --- ARRANGE ---
        val email = "test@example.com"
        val password = "1234"
        val confirmPassword = "1234"
        val onNavigateBack = {}

        coEvery { postUserUseCase.invoke(email, password, confirmPassword) } returns AppResult.Error(AppError.PasswordLength)

        // --- ACT ---
        viewModel.onRegisterClick(email, password, confirmPassword, onNavigateBack)

        // --- ASSERT ---
        assert(viewModel.uiState.value is RegisterViewModel.UIState.Error)
        assert((viewModel.uiState.value as RegisterViewModel.UIState.Error).message == R.string.password_length)
    }

    @Test
    fun `onRegisterClick return Password Mismatch Error`() = runBlocking {
        // --- ARRANGE ---
        val email = "test@example.com"
        val password = "1234567"
        val confirmPassword = "12341111"
        val onNavigateBack = {}

        coEvery { postUserUseCase.invoke(email, password, confirmPassword) } returns AppResult.Error(AppError.PasswordMismatch)

        // --- ACT ---
        viewModel.onRegisterClick(email, password, confirmPassword, onNavigateBack)

        // --- ASSERT ---
        assert(viewModel.uiState.value is RegisterViewModel.UIState.Error)
        assert((viewModel.uiState.value as RegisterViewModel.UIState.Error).message == R.string.password_mismatch)
    }

    @Test
    fun `onRegisterClick return User Already Exists Error`() = runBlocking {
        // --- ARRANGE ---
        val email = "test@example.com"
        val password = "1234567"
        val confirmPassword = "1234567"
        val onNavigateBack = {}

        coEvery { postUserUseCase.invoke(email, password, confirmPassword) } returns AppResult.Error(AppError.UserAlreadyExists)

        // --- ACT ---
        viewModel.onRegisterClick(email, password, confirmPassword, onNavigateBack)

        // --- ASSERT ---
        assert(viewModel.uiState.value is RegisterViewModel.UIState.Error)
        assert((viewModel.uiState.value as RegisterViewModel.UIState.Error).message == R.string.user_already_exists)
    }

    @Test
    fun `onRegisterClick return Unexpected Error`() = runBlocking {
        // --- ARRANGE ---
        val email = "test@example.com"
        val password = "1234567"
        val confirmPassword = "1234567"
        val onNavigateBack = {}

        coEvery { postUserUseCase.invoke(email, password, confirmPassword) } returns AppResult.Error(AppError.Unknown)

        // --- ACT ---
        viewModel.onRegisterClick(email, password, confirmPassword, onNavigateBack)

        // --- ASSERT ---
        assert(viewModel.uiState.value is RegisterViewModel.UIState.Error)
        assert((viewModel.uiState.value as RegisterViewModel.UIState.Error).message == R.string.unexpected_error)
    }

}