package com.example.androidgithubsearch.data.repository

import android.util.Log
import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity
import com.example.androidgithubsearch.data.repository.localdatasource.GitHubLocalDataSourceInterface
import com.example.androidgithubsearch.data.repository.remotedatasource.GitHubRemoteDataSourceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val gitHubRemoteDataSource: GitHubRemoteDataSourceInterface,
    private val gitHubLocalDataSource: GitHubLocalDataSourceInterface
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
                Log.e("GitHubRepository", "Error fetchAndSaveUserRepositories: ${e.message}")
            }
        }
    }

    suspend fun getUserRepositories(): Result<List<UserRepositoryEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(gitHubLocalDataSource.getAllUserRepositories())
            } catch (e: Exception) {
                Log.e("GitHubRepository", "Error getUserRepositories: ${e.message}")
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
                Log.e("GitHubRepository", "Error searchRepositories: ${e.message}")
                Result.failure(e)
            }
        }
    }

    suspend fun addFavoriteRepository(repository: FavoriteRepositoryEntity) {
        return withContext(Dispatchers.IO) {
            try {
                gitHubLocalDataSource.insertFavoriteRepository(repository)
            } catch (e: Exception) {
                Log.e("GitHubRepository", "Error addFavoriteRepository: ${e.message}")
            }
        }
    }

    fun getFavoriteRepositories(): Result<Flow<List<FavoriteRepositoryEntity>>> {
        try {
            return Result.success(gitHubLocalDataSource.getAllFavoriteRepositories())
        } catch (e: Exception) {
            Log.e("GitHubRepository", "Error getFavoriteRepositories: ${e.message}")
            return Result.failure(e)
        }

    }

    suspend fun deleteFavoriteRepository(repository: FavoriteRepositoryEntity) {
        return withContext(Dispatchers.IO) {
            try {
                gitHubLocalDataSource.deleteFavoriteRepository(repository)
            } catch (e: Exception) {
                Log.e("GitHubRepository", "Error deleteFavoriteRepository: ${e.message}")
            }
        }
    }
}