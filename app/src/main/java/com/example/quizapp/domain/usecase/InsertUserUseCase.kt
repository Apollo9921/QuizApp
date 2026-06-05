package com.example.quizapp.domain.usecase

import android.content.Context
import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.presentation.utils.badgesDescription

class InsertUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(context: Context, name: String) {
        val user = UserEntity(
            name = name,
            totalPoints = 0,
            totalPointsPossible = 0,
            badge = context.resources.getString(badgesDescription[0])
        )
        repository.insertUser(user)
    }
}