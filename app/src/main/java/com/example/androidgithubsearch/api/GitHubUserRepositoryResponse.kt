package com.example.androidgithubsearch.api

import com.example.androidgithubsearch.database.entity.UserRepositoryEntity
import com.example.androidgithubsearch.ui.adapter.RepositoryItem
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GitHubUserRepositoryResponse(
    val id: Int,
    val name: String,
    @Json(name = "html_url")
    val url: String,
    val owner: Owner,
    @Json(name = "created_at")
    val created: String,
    @Json(name = "updated_at")
    val updated: String,
    val language: String?,
    @Json(name = "stargazers_count")
    val star: Int,
) {
    data class Owner(
        @Json(name = "avatar_url")
        val avatar: String,
    )
    
    fun toUserRepositoryEntity(): UserRepositoryEntity {
        return UserRepositoryEntity(
            id = this.id,
            name = this.name,
            url = this.url,
            avatar = this.owner.avatar,
            created = dataStringToDate(this.created),
            updated = dataStringToDate(this.updated),
            language = this.language ?: "Unknown",
            star = this.star,
        )
    }
    
    private fun dataStringToDate(dateString: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return formatter.parse(dateString)
    }
}




