package com.example.quizapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizapp.model.results.Results
import com.example.quizapp.model.results.ResultsDAO
import com.example.quizapp.model.user.User
import com.example.quizapp.model.user.UserDAO

@Database(entities = [User::class, Results::class], version = 1, exportSchema = false)
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