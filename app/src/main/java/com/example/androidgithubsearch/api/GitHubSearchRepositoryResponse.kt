package com.example.androidgithubsearch.api

import com.squareup.moshi.Json

data class GitHubSearchRepositoryResponse(
    @field:Json(name = "total_count")
    val total_count: Int,
    val items: List<GitHubUserRepositoryResponse>
)
