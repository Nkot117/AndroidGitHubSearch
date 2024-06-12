package com.example.androidgithubsearch.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteRepositoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,
    val avatar: String,
    val created: String,
    val updated: String,
    val language: String?,
    val star: Int
)