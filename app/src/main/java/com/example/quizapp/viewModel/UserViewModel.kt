package com.example.quizapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.database.QuizDatabase
import com.example.quizapp.model.user.User
import com.example.quizapp.model.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Context): ViewModel() {

    private val repository: UserRepository

    init {
        val userDAO = QuizDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDAO)
    }

    fun createUser(user: User) {
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