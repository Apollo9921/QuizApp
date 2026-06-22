package com.apollo9921.quizrise.data.mapper

import com.apollo9921.quizrise.data.local.entity.UserEntity
import com.apollo9921.quizrise.domain.model.user.User

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