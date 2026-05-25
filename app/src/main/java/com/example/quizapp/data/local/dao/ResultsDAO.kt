package com.example.quizapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizapp.data.local.entity.ResultsEntity

@Dao
interface ResultsDAO {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun createResult(results: ResultsEntity)

    @Query("SELECT * FROM results_table ORDER BY category")
    fun getResults() : LiveData<List<ResultsEntity>>

    @Query("SELECT * FROM results_table WHERE category =:category")
    fun getSpecificCategory(category: String) : LiveData<ResultsEntity>

    @Query("UPDATE results_table SET correctAnswers =:correctAnswers, incorrectAnswers =:incorrectAnswers WHERE category =:category")
    fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int)

}