package com.apollo9921.quizrise.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apollo9921.quizrise.data.local.entity.ResultsEntity
import com.apollo9921.quizrise.data.local.dao.ResultsDAO
import com.apollo9921.quizrise.data.local.entity.UserEntity
import com.apollo9921.quizrise.data.local.dao.UserDAO

@Database(entities = [UserEntity::class, ResultsEntity::class], version = 2, exportSchema = false)
abstract class QuizDatabase: RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun resultsDao(): ResultsDAO
}