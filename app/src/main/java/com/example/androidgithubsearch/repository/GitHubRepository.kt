package com.example.androidgithubsearch.repository

import com.example.androidgithubsearch.api.GitHubApiService
import com.example.androidgithubsearch.model.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.model.GitHubUserRepositoryResponse
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val apiService: GitHubApiService) {
    suspend fun getRepositories(username: String): List<GitHubUserRepositoryResponse> {
        return apiService.getUserRepositories(username)
    }

    suspend fun searchRepositories(query: String): GitHubSearchRepositoryResponse {
        return apiService.searchRepositories(query)
    }
}