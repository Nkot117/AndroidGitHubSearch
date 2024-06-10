package com.example.androidgithubsearch.data.repository.localdatasource

import com.example.androidgithubsearch.data.database.dao.FavoriteRepositoryDao
import com.example.androidgithubsearch.data.database.dao.UserRepositoryDao
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity
import javax.inject.Inject

class GitHubLocalDataSource @Inject constructor(
    private val userRepositoryDao: UserRepositoryDao,
    private val favoriteRepositoryDao: FavoriteRepositoryDao
) : GitHubLocalDataSourceInterface {

    override suspend fun insertUserRepository(userRepositoryEntity: UserRepositoryEntity) {
        userRepositoryDao.insert(userRepositoryEntity)
    }

    override suspend fun getAllUserRepositories(): List<UserRepositoryEntity> {
        return userRepositoryDao.getAll()
    }

    override suspend fun deleteAllUserRepositories() {
        userRepositoryDao.deleteAll()
    }

    override suspend fun insertFavoriteRepository(favoriteRepositoryEntity: FavoriteRepositoryEntity) {
        favoriteRepositoryDao.insert(favoriteRepositoryEntity)
    }

    override suspend fun getAllFavoriteRepositories(): List<FavoriteRepositoryEntity> {
        return favoriteRepositoryDao.getAll()
    }

    override suspend fun deleteFavoriteRepository(favoriteRepositoryEntity: FavoriteRepositoryEntity) {
        favoriteRepositoryDao.delete(favoriteRepositoryEntity)
    }
}