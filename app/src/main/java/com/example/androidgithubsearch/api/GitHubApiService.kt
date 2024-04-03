package com.example.androidgithubsearch.api

import com.example.androidgithubsearch.model.GitHubUserRepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("users/{username}/repos")
    suspend fun getRepositories(@Path("username") username: String): List<GitHubUserRepositoryResponse>
}