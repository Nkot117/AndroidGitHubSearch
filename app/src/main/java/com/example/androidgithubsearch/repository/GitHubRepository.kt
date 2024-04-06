package com.example.androidgithubsearch.repository

import com.example.androidgithubsearch.api.GitHubApiService
import com.example.androidgithubsearch.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.dao.UserRepositoryDao
import com.example.androidgithubsearch.model.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.model.api.toUserRepositoryEntity
import com.example.androidgithubsearch.model.db.FavoriteRepositoryEntity
import com.example.androidgithubsearch.model.db.UserRepositoryEntity
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val apiService: GitHubApiService,
    private val userRepositoryDao: UserRepositoryDao,
    private val favoriteRepositoryDao: FavoriteRepositoryDao
) {
    suspend fun fetchAndSaveUserRepositories(username: String) {
        try {
            val response = apiService.getUserRepositories(username)
            if (response.isNotEmpty()) {
                deleteAllUserRepositories()
                response.map { it.toUserRepositoryEntity() }.forEach {
                    userRepositoryDao.insert(it)
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    suspend fun getUserRepositories(): List<UserRepositoryEntity> {
        return userRepositoryDao.getAll()
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