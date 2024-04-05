package com.example.androidgithubsearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidgithubsearch.dao.UserRepository
import com.example.androidgithubsearch.model.db.UserRepositoryEntity

@Database(entities = [UserRepositoryEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userRepositoryDao(): UserRepository
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}