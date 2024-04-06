package com.example.androidgithubsearch.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidgithubsearch.model.db.UserRepositoryEntity

@Dao
interface UserRepository {
    @Insert
    suspend fun insert(userRepositoryEntity: UserRepositoryEntity)
    
    @Query("SELECT * FROM UserRepositoryEntity")
    suspend fun getAll(): List<UserRepositoryEntity>
    
    @Query("DELETE FROM UserRepositoryEntity")
    suspend fun deleteAll()
}