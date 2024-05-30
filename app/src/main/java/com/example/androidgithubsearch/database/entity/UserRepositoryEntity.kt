package com.example.androidgithubsearch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidgithubsearch.ui.adapter.RepositoryItem
import java.util.Date

@Entity
data class UserRepositoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,
    val avatar: String,
    val created: Date,
    val updated: Date,
    val language: String,
    val star: Int,
) {
    fun toRepositoryItem(): RepositoryItem {
        return   RepositoryItem(
            id = id,
            name = name,
            url = url,
            created = created,
            updated = updated,
            language = language,
            star = star,
            avatar = avatar,
            isFavorite = false
        )
    }
}
