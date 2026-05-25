package com.example.quizapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.data.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Context): ViewModel() {

    private val repository: UserRepositoryImpl

    init {
        val userDAO = QuizDatabase.getDatabase(application).userDao()
        repository = UserRepositoryImpl(userDAO)
    }

    fun createUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createUser(user)
        }
    }

    fun updateUser(totalPoints: Int, totalPointsPossible: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePoints(totalPoints, totalPointsPossible, name)
        }
    }

    fun updateBadge(badge: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBadge(badge, name)
        }
    }

}