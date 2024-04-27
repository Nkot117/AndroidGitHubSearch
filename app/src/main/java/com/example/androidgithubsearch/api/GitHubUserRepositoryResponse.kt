package com.example.androidgithubsearch.api

import com.example.androidgithubsearch.database.entity.UserRepositoryEntity

data class GitHubUserRepositoryResponse(
    val id: Int,
    val name: String,
    val url: String
) {
    fun toUserRepositoryEntity(): UserRepositoryEntity {
        return UserRepositoryEntity(
            id = this.id,
            name = this.name,
            url = this.url
        )
    }
}


