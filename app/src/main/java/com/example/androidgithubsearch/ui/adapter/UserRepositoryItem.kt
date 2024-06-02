package com.example.androidgithubsearch.ui.adapter

import com.example.androidgithubsearch.database.entity.FavoriteRepositoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserRepositoryItem(
    val id: Int,
    val name: String,
    val url: String,
    val created: Date,
    val updated: Date,
    val language: String?,
    val star: Int,
    val avatar: String,
    var isFavorite: Boolean
) {
    fun formatUpdatedDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(this.updated)
    }

    fun toFavoriteRepositoryEntity(): FavoriteRepositoryEntity {
        return FavoriteRepositoryEntity(
            id = id,
            name = name,
            url = url,
        )
    }
}
