package com.apollo9921.quizrise.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apollo9921.quizrise.data.local.entity.ResultsEntity
import com.apollo9921.quizrise.data.local.dao.ResultsDAO
import com.apollo9921.quizrise.data.local.entity.UserEntity
import com.apollo9921.quizrise.data.local.dao.UserDAO

@Database(entities = [UserEntity::class, ResultsEntity::class], version = 1, exportSchema = false)
abstract class QuizDatabase: RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun resultsDao(): ResultsDAO

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}