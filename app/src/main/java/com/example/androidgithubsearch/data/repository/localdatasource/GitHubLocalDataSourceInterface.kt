package com.example.androidgithubsearch.data.repository.localdatasource

import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity

interface GitHubLocalDataSourceInterface {
    suspend fun insertUserRepository(userRepositoryEntity: UserRepositoryEntity)

    suspend fun getAllUserRepositories(): List<UserRepositoryEntity>

    suspend fun deleteAllUserRepositories()

    suspend fun insertFavoriteRepository(favoriteRepositoryEntity: FavoriteRepositoryEntity)

    suspend fun getAllFavoriteRepositories(): List<FavoriteRepositoryEntity>

    suspend fun deleteFavoriteRepository(favoriteRepositoryEntity: FavoriteRepositoryEntity)
}