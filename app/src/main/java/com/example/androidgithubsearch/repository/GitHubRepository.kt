package com.example.androidgithubsearch.repository

import com.example.androidgithubsearch.api.GitHubApiService
import com.example.androidgithubsearch.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.dao.UserRepositoryDao
import com.example.androidgithubsearch.model.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.model.api.toUserRepositoryEntity
import com.example.androidgithubsearch.model.db.FavoriteRepositoryEntity
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val apiService: GitHubApiService,
    private val userRepositoryDao: UserRepositoryDao,
    private val favoriteRepositoryDao: FavoriteRepositoryDao
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

    suspend fun addFavoriteRepository(repository: FavoriteRepositoryEntity) {
        favoriteRepositoryDao.insert(repository)
    }
    
    private suspend fun deleteAllUserRepositories() {
        userRepositoryDao.deleteAll()
    }
}