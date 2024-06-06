package com.example.androidgithubsearch.data.repository

import com.example.androidgithubsearch.data.api.GitHubApiService
import com.example.androidgithubsearch.data.database.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.data.database.dao.UserRepositoryDao
import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity
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

    suspend fun getFavoriteRepositories(): Result<List<FavoriteRepositoryEntity>> {
        return try {
            Result.success(favoriteRepositoryDao.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFavoriteRepository(repository: FavoriteRepositoryEntity) {
        try {
            favoriteRepositoryDao.delete(repository)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    
    private suspend fun deleteAllUserRepositories() {
        userRepositoryDao.deleteAll()
    }
}