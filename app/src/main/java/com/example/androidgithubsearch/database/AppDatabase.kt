package com.example.androidgithubsearch.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidgithubsearch.database.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.database.dao.UserRepositoryDao
import com.example.androidgithubsearch.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.database.entity.UserRepositoryEntity

@Database(entities = [UserRepositoryEntity::class, FavoriteRepositoryEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userRepositoryDao(): UserRepositoryDao
    abstract fun favoriteRepositoryDao(): FavoriteRepositoryDao
}