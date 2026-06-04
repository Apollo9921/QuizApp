package com.example.quizapp.domain.usecase

import android.content.Context
import com.example.quizapp.R
import com.example.quizapp.data.local.entity.UserEntity
import com.example.quizapp.view.custom.badges

class FetchBadgeImageUseCase {
    operator fun invoke(data: UserEntity, context: Context): Int {
        when (data.badge) {
            context.resources.getString(R.string.newbie) -> {
                return badges[0]
            }

            context.resources.getString(R.string.intermediate) -> {
                return badges[1]
            }

            context.resources.getString(R.string.advanced) -> {
                return badges[2]
            }

            context.resources.getString(R.string.legend) -> {
                return badges[3]
            }
        }
        return badges[0]
    }
}