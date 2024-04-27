package com.example.androidgithubsearch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class UserRepositoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,
    val avatar: String,
    val created: String,
    val updated: String,
    val language: String,
    val star: Int,
)
