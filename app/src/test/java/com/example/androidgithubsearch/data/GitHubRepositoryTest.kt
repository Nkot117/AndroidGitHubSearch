package com.example.androidgithubsearch.data

import android.util.Log
import com.example.androidgithubsearch.data.api.GitHubUserRepositoryResponse
import com.example.androidgithubsearch.data.repository.GitHubRepository
import com.example.androidgithubsearch.data.repository.localdatasource.GitHubLocalDataSourceInterface
import com.example.androidgithubsearch.data.repository.remotedatasource.GitHubRemoteDataSourceInterface
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


}