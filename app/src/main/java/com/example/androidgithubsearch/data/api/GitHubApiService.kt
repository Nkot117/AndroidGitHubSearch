package com.example.androidgithubsearch.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") username: String): List<GitHubUserRepositoryResponse>

    @GET("search/repositories")
    suspend fun searchRepositories(@Query("q") query: String, @Query("page") page: Int): GitHubSearchRepositoryResponse
}
