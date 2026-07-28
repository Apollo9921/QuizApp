package com.apollo9921.quizrise.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apollo9921.quizrise.data.local.entity.ResultsEntity

@Dao
interface ResultsDAO {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun createResult(results: ResultsEntity)

    @Query("SELECT * FROM results_table ORDER BY category")
    suspend fun fetchResults() : List<ResultsEntity>

    @Query("SELECT * FROM results_table WHERE category =:category")
    fun getSpecificCategory(category: String) : LiveData<ResultsEntity>

    @Query("UPDATE results_table SET correctAnswers = correctAnswers + :correctAnswers, incorrectAnswers = incorrectAnswers + :incorrectAnswers WHERE category = :category")
    suspend fun updateResults(category: String, correctAnswers: Int, incorrectAnswers: Int)

    @Query("DELETE FROM results_table")
    suspend fun clearAllData()

    @Query("UPDATE results_table SET username = :username WHERE username = :oldUsername")
    suspend fun updateUsername(username: String, oldUsername: String)

}