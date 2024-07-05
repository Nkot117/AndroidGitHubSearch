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
    fun fetch_and_save_user_repositories_success() = runTest {
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
    fun  fetch_and_save_user_repositories_error() = runTest {
        val userName = "TestUser"
        val exceptionMessage = "Network error"
        val logSlot = slot<String>()

        coEvery { gitHubLocalDataSource.deleteAllUserRepositories() } just Runs
        coEvery { gitHubRemoteDataSource.getUserRepositories(userName) } throws Exception(exceptionMessage)

        mockkStatic(Log::class)
        every { Log.e(any(), capture(logSlot)) } returns 0

        gitHubRepository.fetchAndSaveUserRepositories(userName)

        verify { Log.e("GitHubRepository", "Error fetchAndSaveUserRepositories: $exceptionMessage") }

        coVerifySequence {
            gitHubLocalDataSource.deleteAllUserRepositories()
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