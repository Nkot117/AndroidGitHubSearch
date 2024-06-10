package com.example.androidgithubsearch.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity

@Dao
interface FavoriteRepositoryDao {
    @Insert
    suspend fun insert(favoriteRepositoryEntity: FavoriteRepositoryEntity)

    @Delete
    suspend fun delete(favoriteRepositoryEntity: FavoriteRepositoryEntity)

    @Query("SELECT * FROM FavoriteRepositoryEntity")
    suspend fun getAll(): List<FavoriteRepositoryEntity>
}