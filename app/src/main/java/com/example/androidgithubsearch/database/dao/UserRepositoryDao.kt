package com.example.androidgithubsearch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidgithubsearch.database.entity.UserRepositoryEntity

@Dao
interface UserRepositoryDao {
    @Insert
    suspend fun insert(userRepositoryEntity: UserRepositoryEntity)
    
    @Query("SELECT * FROM UserRepositoryEntity")
    suspend fun getAll(): List<UserRepositoryEntity>
    
    @Query("DELETE FROM UserRepositoryEntity")
    suspend fun deleteAll()
}