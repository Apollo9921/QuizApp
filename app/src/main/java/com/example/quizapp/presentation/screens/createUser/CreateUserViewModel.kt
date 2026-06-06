package com.example.quizapp.presentation.screens.createUser

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.quizapp.domain.result.AppResult
import com.example.quizapp.domain.usecase.InsertResultsUseCase
import com.example.quizapp.domain.usecase.InsertUserUseCase
import com.example.quizapp.domain.usecase.SaveUserToRemoteUseCase
import com.example.quizapp.presentation.navigation.Destination
import kotlinx.coroutines.launch

class CreateUserViewModel(
    private val insertUserUseCase: InsertUserUseCase,
    private val insertResultsUseCase: InsertResultsUseCase,
    private val saveUserToRemoteUseCase: SaveUserToRemoteUseCase
) : ViewModel() {

    fun startCreation(context: Context, name: String, navHostController: NavHostController) {
        viewModelScope.launch {
            insertUserUseCase.invoke(name)
            insertResultsUseCase.invoke()
            saveUserAndResults(name, navHostController)
        }
    }

    private fun saveUserAndResults(
        name: String,
        navHostController: NavHostController
    ) {
        viewModelScope.launch {
            val result = saveUserToRemoteUseCase.invoke(name)
            when(result) {
                is AppResult.Error<*> -> {
                    //TODO HANDLE ERROR MESSAGE
                }
                is AppResult.Success<*> -> {
                    navHostController.popBackStack()
                    navHostController.navigate(Destination.Categories.route)
                }
            }
        }
    }
}