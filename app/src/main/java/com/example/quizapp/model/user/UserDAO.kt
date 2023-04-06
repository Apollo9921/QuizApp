package com.example.quizapp.model.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createUser(user: User)

    @Query("SELECT * FROM user_table")
    fun getUserProfile() : LiveData<User>

    @Query("UPDATE user_table SET totalPoints =:totalPoints, totalPointsPossible =:totalPointsPossible WHERE name =:name")
    fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)

    @Query("UPDATE user_table SET badge =:badge WHERE name =:name")
    fun updateBadge(badge: String, name: String)
}