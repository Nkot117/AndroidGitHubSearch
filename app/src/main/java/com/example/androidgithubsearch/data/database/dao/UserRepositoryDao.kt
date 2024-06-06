package com.example.androidgithubsearch.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity

@Dao
interface UserRepositoryDao {
    @Insert
    suspend fun insert(userRepositoryEntity: UserRepositoryEntity)
    
    @Query("SELECT * FROM UserRepositoryEntity ORDER BY id DESC")
    suspend fun getAll(): List<UserRepositoryEntity>
    
    @Query("DELETE FROM UserRepositoryEntity")
    suspend fun deleteAll()
}
