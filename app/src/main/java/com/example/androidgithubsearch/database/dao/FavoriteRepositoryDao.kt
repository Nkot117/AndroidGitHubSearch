package com.example.androidgithubsearch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.androidgithubsearch.database.entity.FavoriteRepositoryEntity

@Dao
interface FavoriteRepositoryDao {
    @Insert
    suspend fun insert(favoriteRepositoryEntity: FavoriteRepositoryEntity)
}