package com.example.androidgithubsearch.data.repository.remotedatasource

import com.example.androidgithubsearch.data.api.GitHubApiService
import com.example.androidgithubsearch.data.api.GitHubUserRepositoryResponse
import javax.inject.Inject

class GitHubRemoteDataSource @Inject constructor(
    private val apiService: GitHubApiService,
) : GitHubRemoteDataSourceInterface {
    override suspend fun getUserRepositories(username: String) = apiService.getUserRepositories(username)
    override suspend fun searchRepositories(query: String, page: Int) = apiService.searchRepositories(query, page)
}