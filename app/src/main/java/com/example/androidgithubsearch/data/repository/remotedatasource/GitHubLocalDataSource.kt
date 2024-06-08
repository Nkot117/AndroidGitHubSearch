package com.example.androidgithubsearch.data.repository.remotedatasource

import com.example.androidgithubsearch.data.database.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.data.database.dao.UserRepositoryDao
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity
import javax.inject.Inject

class GitHubLocalDataSource @Inject constructor(
    private val userRepositoryDao: UserRepositoryDao,
    private val favoriteRepositoryDao: FavoriteRepositoryDao
) {

    suspend fun insertUserRepository(userRepositoryEntity: UserRepositoryEntity) {
        userRepositoryDao.insert(userRepositoryEntity)
    }

    suspend fun getAllUserRepositories(): List<UserRepositoryEntity> {
        return userRepositoryDao.getAll()
    }

    suspend fun deleteAllUserRepositories() {
        userRepositoryDao.deleteAll()
    }

    suspend fun insertFavoriteRepository(favoriteRepositoryEntity: FavoriteRepositoryEntity) {
        favoriteRepositoryDao.insert(favoriteRepositoryEntity)
    }

    suspend fun getAllFavoriteRepositories(): List<FavoriteRepositoryEntity> {
        return favoriteRepositoryDao.getAll()
    }

    suspend fun deleteFavoriteRepository(favoriteRepositoryEntity: FavoriteRepositoryEntity) {
        favoriteRepositoryDao.delete(favoriteRepositoryEntity)
    }
}