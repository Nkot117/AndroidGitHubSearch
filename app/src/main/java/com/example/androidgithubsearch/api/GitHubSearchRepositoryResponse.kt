package com.example.androidgithubsearch.api

import com.example.androidgithubsearch.ui.adapter.RepositoryItem
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

        fun toRepositoryItem(): RepositoryItem {
            return RepositoryItem(
                name = name,
                url = url,
                created = dateStringToDate(created),
                updated = dateStringToDate(updated),
                language = language ?: "Unknown",
                star = star,
                avatar = owner.avatar
            )
        }

        private fun dateStringToDate(dateString: String): Date {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            return formatter.parse(dateString)
        }
    }
}
