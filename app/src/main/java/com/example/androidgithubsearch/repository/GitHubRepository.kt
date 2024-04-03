package com.example.androidgithubsearch.repository

import com.example.androidgithubsearch.api.GitHubApiService
import com.example.androidgithubsearch.model.GitHubUserRepositoryResponse
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val apiService: GitHubApiService) {
    suspend fun getRepositories(username: String): List<GitHubUserRepositoryResponse> {
        return apiService.getRepositories(username)
    }
}