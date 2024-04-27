package com.example.androidgithubsearch.api

import com.example.androidgithubsearch.database.entity.UserRepositoryEntity
import com.squareup.moshi.Json

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
            created = this.created,
            updated = this.updated,
            language = this.language ?: "Unknown",
            star = this.star,
        )
    }
}




