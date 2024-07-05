package com.example.androidgithubsearch.data

import com.example.androidgithubsearch.data.api.GitHubApiService
import com.example.androidgithubsearch.data.repository.remotedatasource.GitHubRemoteDataSource
import com.example.androidgithubsearch.data.repository.remotedatasource.GitHubRemoteDataSourceInterface
import com.example.androidgithubsearch.setBodyFromFileName
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GitHubRemoteDataSourceTest {
    private lateinit var gitHubRemoteDataSource: GitHubRemoteDataSourceInterface
    private lateinit var mockService: MockWebServer

    @Before
    fun setUp() {
        mockService = MockWebServer()
        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path == null) return MockResponse().setResponseCode(400)
                return when {
                    request.path!!.matches(Regex("/users/test/repos")) -> {
                        MockResponse().setResponseCode(200).setBodyFromFileName("not_found.json")
                    }

                    request.path!!.matches(Regex("/users/[a-zA-Z0-9]+/repos")) -> {
                        MockResponse().setResponseCode(200).setBodyFromFileName("user_repos.json")
                    }

                    request.path!!.matches(Regex("/search/repositories\\?q=[a-zA-Z0-9]+&page=[0-9]+")) -> {
                        MockResponse().setResponseCode(200).setBodyFromFileName("search_repos.json")
                    }

                    else -> {
                        MockResponse().setResponseCode(400)
                    }
                }
            }
        }
        mockService.dispatcher = dispatcher
        mockService.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockService.url(""))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        gitHubRemoteDataSource =
            GitHubRemoteDataSource(retrofit.create(GitHubApiService::class.java))
    }

    @After
    fun tearDown() {
        mockService.shutdown()
    }

    @Test
    fun `getUserRepositories_リモートのユーザーリポジトリが取得できること`() = runTest {
        val response = gitHubRemoteDataSource.getUserRepositories("username")
        assertThat(response).also {
            it.isNotEmpty()
            it.hasSize(6)
        }
    }

    @Test
    fun `getUserRepositories_ユーザーリポジトリが取得できない場合、例外がスローされること_存在しないユーザー名`() = runTest {
        try {
         gitHubRemoteDataSource.getUserRepositories("test")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
    }

    @Test
    fun `getUserRepositories_ユーザーリポジトリが取得できない場合、例外がスローされること_空文字`() = runTest {
        try {
            gitHubRemoteDataSource.getUserRepositories("")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
    }

    @Test
    fun `searchRepositories_検索結果が取得できること`() = runTest {
        val response = gitHubRemoteDataSource.searchRepositories("search",1)
        assertThat(response.totalCount).also {
            it.isNotNull()
            it.isEqualTo(30)
        }
        assertThat(response.items).also {
            it.isNotEmpty()
            it.hasSize(30)
        }
    }

    @Test
    fun `searchRepositories_検索結果が取得できない場合、例外がスローされること_検索文字が空文字`() = runTest {
        try {
            gitHubRemoteDataSource.searchRepositories("",1)
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
    }
}