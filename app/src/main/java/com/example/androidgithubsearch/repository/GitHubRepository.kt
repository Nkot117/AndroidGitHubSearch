package com.example.androidgithubsearch.repository

import com.example.androidgithubsearch.api.GitHubApiService
import com.example.androidgithubsearch.database.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.database.dao.UserRepositoryDao
import com.example.androidgithubsearch.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.database.entity.UserRepositoryEntity
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val apiService: GitHubApiService,
    private val userRepositoryDao: UserRepositoryDao,
    private val favoriteRepositoryDao: FavoriteRepositoryDao
) {
    suspend fun fetchAndSaveUserRepositories(username: String) {
        try {
            deleteAllUserRepositories()
            val response = apiService.getUserRepositories(username)
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

    suspend fun searchRepositories(
        query: String,
        page: Int
    ): Result<GitHubSearchRepositoryResponse> {
        return try {
            Result.success(apiService.searchRepositories(query, page))
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