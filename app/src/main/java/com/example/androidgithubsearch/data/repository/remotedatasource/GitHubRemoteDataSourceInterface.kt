package com.example.androidgithubsearch.data.repository.remotedatasource

import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.api.GitHubUserRepositoryResponse

interface GitHubRemoteDataSourceInterface {
    suspend fun searchRepositories(query: String, page: Int) : GitHubSearchRepositoryResponse
    suspend fun getUserRepositories(username: String) : List<GitHubUserRepositoryResponse>
}