package com.example.androidgithubsearch.data.repository

import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity
import com.example.androidgithubsearch.data.repository.localdatasource.GitHubRemoteDataSource
import com.example.androidgithubsearch.data.repository.remotedatasource.GitHubLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val gitHubRemoteDataSource: GitHubRemoteDataSource,
    private val gitHubLocalDataSource: GitHubLocalDataSource
) {
    suspend fun fetchAndSaveUserRepositories(username: String) {
        return withContext(Dispatchers.IO) {
            try {
                gitHubLocalDataSource.deleteAllUserRepositories()
                val response = gitHubRemoteDataSource.getUserRepositories(username)
                response.map { it.toUserRepositoryEntity() }.forEach {
                    gitHubLocalDataSource.insertUserRepository(it)
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

    suspend fun getUserRepositories(): Result<List<UserRepositoryEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(gitHubLocalDataSource.getAllUserRepositories())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun searchRepositories(
        query: String,
        page: Int
    ): Result<GitHubSearchRepositoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(gitHubRemoteDataSource.searchRepositories(query, page))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun addFavoriteRepository(repository: FavoriteRepositoryEntity) {
        return withContext(Dispatchers.IO) {
            try {
                gitHubLocalDataSource.insertFavoriteRepository(repository)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

    suspend fun getFavoriteRepositories(): Result<List<FavoriteRepositoryEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(gitHubLocalDataSource.getAllFavoriteRepositories())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteFavoriteRepository(repository: FavoriteRepositoryEntity) {
        return withContext(Dispatchers.IO) {
            try {
                gitHubLocalDataSource.deleteFavoriteRepository(repository)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}