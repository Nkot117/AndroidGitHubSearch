package com.example.androidgithubsearch.model.api

import com.example.androidgithubsearch.model.db.UserRepositoryEntity

data class GitHubUserRepositoryResponse(
    val id: Int,
    val name: String,
    val url: String
)

fun GitHubUserRepositoryResponse.toUserRepositoryEntity(): UserRepositoryEntity {
    return UserRepositoryEntity(
        id = this.id,
        name = this.name,
        url = this.url
    )
}
