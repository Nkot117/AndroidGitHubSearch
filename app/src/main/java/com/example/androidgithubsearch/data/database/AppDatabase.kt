package com.example.androidgithubsearch.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidgithubsearch.data.database.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.data.database.dao.UserRepositoryDao
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity

@Database(entities = [UserRepositoryEntity::class, FavoriteRepositoryEntity::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userRepositoryDao(): UserRepositoryDao
    abstract fun favoriteRepositoryDao(): FavoriteRepositoryDao
}
