package com.example.androidgithubsearch.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.example.androidgithubsearch.data.database.AppDatabase
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.database.entity.UserRepositoryEntity
import com.example.androidgithubsearch.data.repository.localdatasource.GitHubLocalDataSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.util.Date


@RunWith(Enclosed::class)
class GitHubLocalDataSourceTest {

    abstract class DBTest {
        lateinit var gitHubLocalDataSource: GitHubLocalDataSource
        lateinit var db: AppDatabase

        @Before
        fun setUp() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            db = Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
                .allowMainThreadQueries().build()
            gitHubLocalDataSource =
                GitHubLocalDataSource(db.userRepositoryDao(), db.favoriteRepositoryDao())
        }

        @After
        fun tearDown() {
            db.close()
        }
    }

    @RunWith(AndroidJUnit4::class)
    class BlankRecordTest : DBTest() {
        @Test
        fun insert_user_repository() = runTest {
            val userRepositoryEntity = UserRepositoryEntity(
                id = 1,
                name = "name",
                url = "url",
                avatar = "avatar",
                created = Date(),
                updated = Date(),
                language = "language",
                star = 1
            )
            gitHubLocalDataSource.insertUserRepository(userRepositoryEntity)

            val list = gitHubLocalDataSource.getAllUserRepositories()

            assertThat(list).hasSize(1)
        }

        @Test
        fun insert_favorite_repository() = runTest {
            val favoriteRepositoryEntity = FavoriteRepositoryEntity(
                id = 1,
                name = "name",
                url = "url",
                avatar = "avatar",
                created = Date().toString(),
                updated = Date().toString(),
                language = "language",
                star = 1
            )
            gitHubLocalDataSource.insertFavoriteRepository(favoriteRepositoryEntity)

            val list = gitHubLocalDataSource.getAllFavoriteRepositories()
            list.test {
                assertThat(awaitItem()).hasSize(1)
            }
        }
    }

    @RunWith(AndroidJUnit4::class)
    class NonBlankRecordTest : DBTest() {
        @Before
        fun setUpDBRecord() = runTest {
            val userRepositoryEntityList = listOf(
                UserRepositoryEntity(
                    id = 1,
                    name = "name",
                    url = "url",
                    avatar = "avatar",
                    created = Date(),
                    updated = Date(),
                    language = "language",
                    star = 1
                ),
                UserRepositoryEntity(
                    id = 2,
                    name = "name",
                    url = "url",
                    avatar = "avatar",
                    created = Date(),
                    updated = Date(),
                    language = "language",
                    star = 1
                ),
                UserRepositoryEntity(
                    id = 3,
                    name = "name",
                    url = "url",
                    avatar = "avatar",
                    created = Date(),
                    updated = Date(),
                    language = "language",
                    star = 1
                )
            )

            userRepositoryEntityList.map {
                gitHubLocalDataSource.insertUserRepository(it)
            }

            val favoriteRepositoryEntityList = listOf(
                FavoriteRepositoryEntity(
                    id = 1,
                    name = "name",
                    url = "url",
                    avatar = "avatar",
                    created = Date().toString(),
                    updated = Date().toString(),
                    language = "language",
                    star = 1
                ),
                FavoriteRepositoryEntity(
                    id = 2,
                    name = "name",
                    url = "url",
                    avatar = "avatar",
                    created = Date().toString(),
                    updated = Date().toString(),
                    language = "language",
                    star = 1
                ),
                FavoriteRepositoryEntity(
                    id = 3,
                    name = "name",
                    url = "url",
                    avatar = "avatar",
                    created = Date().toString(),
                    updated = Date().toString(),
                    language = "language",
                    star = 1
                )
            )

            favoriteRepositoryEntityList.map {
                gitHubLocalDataSource.insertFavoriteRepository(it)
            }
        }

        @Test
        fun get_all_user_repositories() = runTest {
            val list = gitHubLocalDataSource.getAllUserRepositories()
            assertThat(list).hasSize(3)
        }

        @Test
        fun get_all_favorite_repositories() = runTest {
            val list = gitHubLocalDataSource.getAllFavoriteRepositories()
            list.test {
                assertThat(awaitItem()).hasSize(3)
            }
        }

        @Test
        fun delete_all_user_repositories() = runTest {
            gitHubLocalDataSource.deleteAllUserRepositories()
            val list = gitHubLocalDataSource.getAllUserRepositories()
            assertThat(list).isEmpty()
        }

        @Test
        fun delete_all_favorite_repositories() = runTest {
            gitHubLocalDataSource.deleteFavoriteRepository(FavoriteRepositoryEntity(
                id = 1,
                name = "name",
                url = "url",
                avatar = "avatar",
                created = Date().toString(),
                updated = Date().toString(),
                language = "language",
                star = 1
            ))
            val list = gitHubLocalDataSource.getAllFavoriteRepositories()
            list.test {
                assertThat(awaitItem()).hasSize(2)
            }
        }
    }
}