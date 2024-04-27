package com.example.androidgithubsearch.ui.adapter

data class RepositoryItem(
    val name: String,
    val url: String,
    val created: String,
    val updated: String,
    val language: String?,
    val star: Int,
)
