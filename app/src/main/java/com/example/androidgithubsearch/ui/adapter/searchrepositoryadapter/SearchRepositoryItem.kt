package com.example.androidgithubsearch.ui.adapter.searchrepositoryadapter

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SearchRepositoryItem(
    val id: Int,
    val name: String,
    val url: String,
    val created: Date,
    val updated: Date,
    val language: String?,
    val star: Int,
    val avatar: String,
    val isFavorite: Boolean,
    val clickAddFavoriteAction: () -> Unit,
    val clickRemoveFavoriteAction: () -> Unit,
    val clickItemAction: () -> Unit,
) {
    fun formatUpdatedDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(this.updated)
    }
}
