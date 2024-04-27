package com.example.androidgithubsearch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRepositoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String
)
