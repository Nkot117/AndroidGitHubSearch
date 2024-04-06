package com.example.androidgithubsearch.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.androidgithubsearch.model.db.FavoriteRepositoryEntity

@Dao
interface FavoriteRepositoryDao {
    @Insert
    suspend fun insert(favoriteRepositoryEntity: FavoriteRepositoryEntity)
}