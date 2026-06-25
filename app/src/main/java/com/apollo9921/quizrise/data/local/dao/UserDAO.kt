package com.apollo9921.quizrise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apollo9921.quizrise.data.local.entity.UserEntity

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_table")
    fun fetchUser() : UserEntity

    @Query("UPDATE user_table SET totalPoints = totalPoints + :totalPoints, totalPointsPossible = totalPointsPossible + :totalPointsPossible WHERE name =:name")
    suspend fun updatePoints(totalPoints: Int, totalPointsPossible: Int, name: String)

    @Query("UPDATE user_table SET badge =:badge WHERE name =:name")
    fun updateBadge(badge: String, name: String)

    @Query("DELETE FROM user_table")
    suspend fun clearAllData()
}