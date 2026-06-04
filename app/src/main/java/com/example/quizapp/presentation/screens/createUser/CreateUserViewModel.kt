package com.example.quizapp.presentation.screens.createUser

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.quizapp.domain.usecase.InsertResultsUseCase
import com.example.quizapp.domain.usecase.InsertUserUseCase
import com.example.quizapp.presentation.navigation.Destination
import com.example.quizapp.presentation.userManager
import kotlinx.coroutines.launch

class CreateUserViewModel(
    private val insertUserUseCase: InsertUserUseCase,
    private val insertResultsUseCase: InsertResultsUseCase
) : ViewModel() {

    fun startCreation(context: Context, name: String, navHostController: NavHostController) {
        viewModelScope.launch {
            insertUserUseCase.invoke(context, name)
            insertResultsUseCase.invoke(context)
            userManager.storeToDataStore(true, name)
            navHostController.popBackStack()
            navHostController.navigate(Destination.Categories.route)
        }
    }
}