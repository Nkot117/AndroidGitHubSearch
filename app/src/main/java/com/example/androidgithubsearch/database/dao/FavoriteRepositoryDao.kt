package com.example.androidgithubsearch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidgithubsearch.database.entity.FavoriteRepositoryEntity

@Dao
interface FavoriteRepositoryDao {
    @Insert
    suspend fun insert(favoriteRepositoryEntity: FavoriteRepositoryEntity)

    @Query("SELECT * FROM FavoriteRepositoryEntity")
    suspend fun getAll(): List<FavoriteRepositoryEntity>
}