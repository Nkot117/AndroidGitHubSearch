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
            deleteAllUserRepositories()
            response.map { it.toUserRepositoryEntity() }.forEach {
                userRepositoryDao.insert(it)
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    
    suspend fun getUserRepositories(): Result<List<UserRepositoryEntity>> {
        return try {
            Result.success(userRepositoryDao.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchRepositories(query: String): Result<GitHubSearchRepositoryResponse> {
        return try {
            Result.success(apiService.searchRepositories(query))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addFavoriteRepository(repository: FavoriteRepositoryEntity) {
        try {
            favoriteRepositoryDao.insert(repository)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    
    private suspend fun deleteAllUserRepositories() {
        userRepositoryDao.deleteAll()
    }
}