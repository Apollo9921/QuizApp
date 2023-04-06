package com.example.quizapp.model.results

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ResultsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createResult(results: Results)

    @Query("SELECT * FROM results_table ORDER BY category")
    fun getResults() : LiveData<List<Results>>

    @Query("SELECT * FROM results_table WHERE category =:category")
    fun getSpecificCategory(category: String) : LiveData<Results>

    @Query("UPDATE results_table SET correctAnswers =:correctAnswers, incorrectAnswers =:incorrectAnswers WHERE category =:category")
    fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int)

}