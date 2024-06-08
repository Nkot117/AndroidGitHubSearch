package com.example.androidgithubsearch.data.repository.localdatasource

import com.example.androidgithubsearch.data.api.GitHubApiService
import javax.inject.Inject

class GitHubRemoteDataSource @Inject constructor(
    private val apiService: GitHubApiService,
) {
    suspend fun searchRepositories(query: String, page: Int) = apiService.searchRepositories(query, page)

    suspend fun getUserRepositories(username: String) = apiService.getUserRepositories(username)
}