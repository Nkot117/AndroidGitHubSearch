package com.example.androidgithubsearch.repository

import com.example.androidgithubsearch.api.GitHubApiService
import com.example.androidgithubsearch.dao.UserRepositoryDao
import com.example.androidgithubsearch.model.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.model.api.toUserRepositoryEntity
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val apiService: GitHubApiService,
    private val userRepositoryDao: UserRepositoryDao
){
    suspend fun fetchAndSaveUserRepositories(username: String) {
        deleteAllUserRepositories()
        val response = apiService.getUserRepositories(username)
        response.map { it.toUserRepositoryEntity() }.forEach {
             userRepositoryDao.insert(it)
        }
    }

    suspend fun searchRepositories(query: String): GitHubSearchRepositoryResponse {
        return apiService.searchRepositories(query)
    }
    
    private suspend fun deleteAllUserRepositories() {
        userRepositoryDao.deleteAll()
    }
}