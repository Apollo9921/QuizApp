package com.example.quizapp.data.mapper

import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.domain.model.user.User

fun UserEntity.toUser(): User {
    return User(
        name = name,
        totalPoints = totalPoints,
        totalPointsPossible = totalPointsPossible,
        badge = badge
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        name = name,
        totalPoints = totalPoints,
        totalPointsPossible = totalPointsPossible,
        badge = badge
    )
}