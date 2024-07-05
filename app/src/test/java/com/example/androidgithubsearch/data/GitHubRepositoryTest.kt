package com.example.androidgithubsearch.data

import android.util.Log
import app.cash.turbine.test
import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.api.GitHubUserRepositoryResponse
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.repository.GitHubRepository
import com.example.androidgithubsearch.data.repository.localdatasource.GitHubLocalDataSourceInterface
import com.example.androidgithubsearch.data.repository.remotedatasource.GitHubRemoteDataSourceInterface
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GitHubRepositoryTest {
    private lateinit var gitHubRemoteDataSource: GitHubRemoteDataSourceInterface
    private lateinit var gitHubLocalDataSource: GitHubLocalDataSourceInterface
    private lateinit var gitHubRepository: GitHubRepository

    @Before
    fun setUp() {
        gitHubRemoteDataSource = mockk()
        gitHubLocalDataSource = mockk()
        gitHubRepository = GitHubRepository(gitHubRemoteDataSource, gitHubLocalDataSource)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `fetchAndSaveUserRepositories_ユーザーリポジトリを取得し・保存できること`() = runTest {
        val userName = "TestUser"
        val remoteResponse = getMockGitHubUserRepositoryResponse()

        coEvery { gitHubLocalDataSource.deleteAllUserRepositories() } just Runs
        coEvery { gitHubLocalDataSource.insertUserRepository(any()) } just Runs
        coEvery { gitHubRemoteDataSource.getUserRepositories(userName) } returns remoteResponse

        gitHubRepository.fetchAndSaveUserRepositories(userName)

        coVerifySequence {
            gitHubLocalDataSource.deleteAllUserRepositories()
            gitHubRemoteDataSource.getUserRepositories(userName)
            remoteResponse.forEach {
                gitHubLocalDataSource.insertUserRepository(it.toUserRepositoryEntity())
            }
        }
    }

    @Test
    fun `fetchAndSaveUserRepositories_リポジトリ削除に失敗した場合、ログ出力されること`() = runTest {
        val userName = "TestUser"
        val exceptionMessage = "Delete Repository Error"
        val logSlot = slot<String>()

        coEvery { gitHubLocalDataSource.deleteAllUserRepositories() } throws Exception(
            exceptionMessage
        )

        mockkStatic(Log::class)
        every { Log.e(any(), capture(logSlot)) } returns 0

        gitHubRepository.fetchAndSaveUserRepositories(userName)

        verify {
            Log.e(
                "GitHubRepository",
                "Error fetchAndSaveUserRepositories: $exceptionMessage"
            )
        }

        coVerify { gitHubLocalDataSource.deleteAllUserRepositories() }
    }

    @Test
    fun `fetchAndSaveUserRepositories_リポジトリ取得に失敗した場合、ログ出力されること`() = runTest {
        val userName = "TestUser"
        val exceptionMessage = "Get Repository Error"
        val logSlot = slot<String>()

        coEvery { gitHubLocalDataSource.deleteAllUserRepositories() } just Runs
        coEvery { gitHubRemoteDataSource.getUserRepositories(userName) } throws Exception(
            exceptionMessage
        )

        mockkStatic(Log::class)
        every { Log.e(any(), capture(logSlot)) } returns 0

        gitHubRepository.fetchAndSaveUserRepositories(userName)

        verify {
            Log.e(
                "GitHubRepository",
                "Error fetchAndSaveUserRepositories: $exceptionMessage"
            )
        }

        coVerifySequence {
            gitHubLocalDataSource.deleteAllUserRepositories()
            gitHubRemoteDataSource.getUserRepositories(userName)
        }
    }

    @Test
    fun `fetchAndSaveUserRepositories_リポジトリ保存に失敗した場合、ログ出力されること`() = runTest {
        val userName = "TestUser"
        val exceptionMessage = "Save Repository Error"
        val logSlot = slot<String>()

        coEvery { gitHubLocalDataSource.deleteAllUserRepositories() } just Runs
        coEvery { gitHubRemoteDataSource.getUserRepositories(userName) } returns getMockGitHubUserRepositoryResponse()
        coEvery { gitHubLocalDataSource.insertUserRepository(any()) } throws Exception(
            exceptionMessage
        )

        mockkStatic(Log::class)
        every { Log.e(any(), capture(logSlot)) } returns 0

        gitHubRepository.fetchAndSaveUserRepositories(userName)

        verify {
            Log.e(
                "GitHubRepository",
                "Error fetchAndSaveUserRepositories: $exceptionMessage"
            )
        }

        coVerifySequence {
            gitHubLocalDataSource.deleteAllUserRepositories()
            gitHubRemoteDataSource.getUserRepositories(userName)
            gitHubLocalDataSource.insertUserRepository(any())
        }
    }

    @Test
    fun `getUserRepositories_ユーザーリポジトリを取得できること`() = runTest {
        val localResponse = getMockGitHubUserRepositoryResponse().map {
            it.toUserRepositoryEntity()
        }

        coEvery { gitHubLocalDataSource.getAllUserRepositories() } returns localResponse

        val result = gitHubRepository.getUserRepositories()

        coVerify { gitHubLocalDataSource.getAllUserRepositories() }

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(localResponse)
    }

    @Test
    fun `getUserRepositories_ユーザーリポジトリを取得できない場合、空のリストを返すこと`() =
        runTest {
            val exceptionMessage = "Get Repository Error"
            val logSlot = slot<String>()

            coEvery { gitHubLocalDataSource.getAllUserRepositories() } throws Exception(
                exceptionMessage
            )

            mockkStatic(Log::class)
            every { Log.e(any(), capture(logSlot)) } returns 0

            val result = gitHubRepository.getUserRepositories()

            coVerify { gitHubLocalDataSource.getAllUserRepositories() }

            verify {
                Log.e("GitHubRepository", "Error getUserRepositories: $exceptionMessage")
            }

            assertThat(result.isFailure).isTrue()
            assertThat(result.getOrNull()).isNull()
        }

    @Test
    fun `searchRepositories_ユーザーリポジトリを検索し、取得できること`() = runTest {
        val localResponse = getMockGitHubSearchRepositoryResponse()
        val query = "test"
        val page = 1

        coEvery { gitHubRemoteDataSource.searchRepositories(query, page) } returns localResponse

        val result = gitHubRepository.searchRepositories(query, page)

        coVerify { gitHubRemoteDataSource.searchRepositories(query, page) }

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(localResponse)
    }

    @Test
    fun `searchRepositories_ユーザーリポジトリを検索して取得できない場合、空のリストを返すこと`() =
        runTest {
            val exceptionMessage = "Search Repository Error"
            val logSlot = slot<String>()
            val query = "test"
            val page = 1

            coEvery { gitHubRemoteDataSource.searchRepositories(query, page) } throws Exception(
                exceptionMessage
            )

            mockkStatic(Log::class)
            every { Log.e(any(), capture(logSlot)) } returns 0

            val result = gitHubRepository.searchRepositories(query, page)

            coVerify { gitHubRemoteDataSource.searchRepositories(query, page) }

            verify {
                Log.e("GitHubRepository", "Error searchRepositories: $exceptionMessage")
            }

            assertThat(result.isFailure).isTrue()
            assertThat(result.getOrNull()).isNull()
        }

    @Test
    fun `addFavoriteRepository_お気に入りリポジトリを追加できること`() = runTest {
        val repository = getMockGitHubUserRepositoryResponse().map {
            FavoriteRepositoryEntity(
                id = it.id,
                name = it.name,
                url = it.url,
                avatar = it.owner.avatar,
                created = it.created,
                updated = it.updated,
                language = it.language,
                star = it.star
            )
        }.first()

        coEvery { gitHubLocalDataSource.insertFavoriteRepository(repository) } just Runs

        gitHubRepository.addFavoriteRepository(repository)

        coVerify { gitHubLocalDataSource.insertFavoriteRepository(repository) }
    }

    @Test
    fun `addFavoriteRepository_お気に入りリポジトリ追加に失敗した場合、ログ出力されること`() =
        runTest {
            val repository = getMockGitHubUserRepositoryResponse().map {
                FavoriteRepositoryEntity(
                    id = it.id,
                    name = it.name,
                    url = it.url,
                    avatar = it.owner.avatar,
                    created = it.created,
                    updated = it.updated,
                    language = it.language,
                    star = it.star
                )
            }.first()

            val exceptionMessage = "Add Favorite Repository Error"
            val logSlot = slot<String>()

            mockkStatic(Log::class)
            every { Log.e(any(), capture(logSlot)) } returns 0

            coEvery { gitHubLocalDataSource.insertFavoriteRepository(repository) } throws Exception(
                exceptionMessage
            )

            gitHubRepository.addFavoriteRepository(repository)

            verify {
                Log.e("GitHubRepository", "Error addFavoriteRepository: $exceptionMessage")
            }

            coVerify { gitHubLocalDataSource.insertFavoriteRepository(repository) }
        }

    @Test
    fun `getFavoriteRepositories_お気に入りリポジトリを取得できること`() = runTest {
        val localResponse = getMockGitHubUserRepositoryResponse().map {
            FavoriteRepositoryEntity(
                id = it.id,
                name = it.name,
                url = it.url,
                avatar = it.owner.avatar,
                created = it.created,
                updated = it.updated,
                language = it.language,
                star = it.star
            )
        }

        coEvery { gitHubLocalDataSource.getAllFavoriteRepositories() } returns flowOf(localResponse)

        val result = gitHubRepository.getFavoriteRepositories()

        assertThat(result.isSuccess).isTrue()
        val resultValue = result.getOrNull()
        assertThat(resultValue).isNotNull()
        resultValue?.test {
            assertThat(awaitItem()).isEqualTo(localResponse)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getFavoriteRepositories_お気に入りリポジトリを取得できない場合、空のリストを返すこと`() = runTest {
        val exceptionMessage = "Get Favorite Repository Error"
        val logSlot = slot<String>()

        coEvery { gitHubLocalDataSource.getAllFavoriteRepositories() } throws Exception(
            exceptionMessage
        )

        mockkStatic(Log::class)
        every { Log.e(any(), capture(logSlot)) } returns 0

        val result = gitHubRepository.getFavoriteRepositories()

        coVerify { gitHubLocalDataSource.getAllFavoriteRepositories() }

        verify {
            Log.e("GitHubRepository", "Error getFavoriteRepositories: $exceptionMessage")
        }

        assertThat(result.isFailure).isTrue()
        assertThat(result.getOrNull()).isNull()

    }

    @Test
    fun `deleteFavoriteRepository_お気に入りリポジトリを削除できること`() = runTest {
        val repository = getMockGitHubUserRepositoryResponse().map {
            FavoriteRepositoryEntity(
                id = it.id,
                name = it.name,
                url = it.url,
                avatar = it.owner.avatar,
                created = it.created,
                updated = it.updated,
                language = it.language,
                star = it.star
            )
        }.first()

        coEvery { gitHubLocalDataSource.deleteFavoriteRepository(repository) } just Runs

        gitHubRepository.deleteFavoriteRepository(repository)

        coVerify { gitHubLocalDataSource.deleteFavoriteRepository(repository) }
    }

    @Test
    fun `deleteFavoriteRepository_お気に入りリポジトリ削除に失敗した場合、ログ出力されること`() = runTest {
        val repository = getMockGitHubUserRepositoryResponse().map {
            FavoriteRepositoryEntity(
                id = it.id,
                name = it.name,
                url = it.url,
                avatar = it.owner.avatar,
                created = it.created,
                updated = it.updated,
                language = it.language,
                star = it.star
            )
        }.first()

        val exceptionMessage = "Delete Favorite Repository Error"
        val logSlot = slot<String>()

        mockkStatic(Log::class)
        every { Log.e(any(), capture(logSlot)) } returns 0

        coEvery { gitHubLocalDataSource.deleteFavoriteRepository(repository) } throws Exception(
            exceptionMessage
        )

        gitHubRepository.deleteFavoriteRepository(repository)

        verify {
            Log.e("GitHubRepository", "Error deleteFavoriteRepository: $exceptionMessage")
        }

        coVerify { gitHubLocalDataSource.deleteFavoriteRepository(repository) }
    }

}


private fun getMockGitHubUserRepositoryResponse(): List<GitHubUserRepositoryResponse> {
    return listOf(
        GitHubUserRepositoryResponse(
            id = 1,
            name = "name",
            url = "url",
            owner = GitHubUserRepositoryResponse.Owner("avatar"),
            created = "2021-01-01T00:00:00Z",
            updated = "2021-01-01T00:00:00Z",
            language = "language",
            star = 1
        ),
        GitHubUserRepositoryResponse(
            id = 2,
            name = "name",
            url = "url",
            owner = GitHubUserRepositoryResponse.Owner("avatar"),
            created = "2021-01-01T00:00:00Z",
            updated = "2021-01-01T00:00:00Z",
            language = "language",
            star = 1
        ),
        GitHubUserRepositoryResponse(
            id = 3,
            name = "name",
            url = "url",
            owner = GitHubUserRepositoryResponse.Owner("avatar"),
            created = "2021-01-01T00:00:00Z",
            updated = "2021-01-01T00:00:00Z",
            language = "language",
            star = 1
        )
    )
}

private fun getMockGitHubSearchRepositoryResponse(): GitHubSearchRepositoryResponse {
    return GitHubSearchRepositoryResponse(
        totalCount = 30,
        items = getMockGitHubUserRepositoryResponse().map {
            GitHubSearchRepositoryResponse.Item(
                id = it.id,
                name = it.name,
                url = it.url,
                owner = GitHubSearchRepositoryResponse.Item.Owner(
                    login = "test",
                    avatar = it.owner.avatar
                ),
                created = it.created,
                updated = it.updated,
                language = it.language,
                star = it.star
            )
        }
    )

}