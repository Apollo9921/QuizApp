package com.example.quizapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.domain.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository): ViewModel() {

    fun createUser(user: UserEntity) {
        viewModelScope.launch {
            repository.createUser(user)
        }
    }

    fun updateUser(totalPoints: Int, totalPointsPossible: Int, name: String) {
        viewModelScope.launch {
            repository.updatePoints(totalPoints, totalPointsPossible, name)
        }
    }

    fun updateBadge(badge: String, name: String) {
        viewModelScope.launch {
            repository.updateBadge(badge, name)
        }
    }

}