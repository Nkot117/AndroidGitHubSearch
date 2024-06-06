package com.example.androidgithubsearch.data.api

import com.example.androidgithubsearch.ui.fragment.searchrepository.SearchRepositoryItem
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GitHubSearchRepositoryResponse(
    @Json(name = "total_count")
    val totalCount: Int,
    val items: List<Item>
) {
    data class Item(
        val id: Int,
        @Json(name = "full_name")
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
            val login: String,
            @Json(name = "avatar_url")
            val avatar: String
        )
    }
}
