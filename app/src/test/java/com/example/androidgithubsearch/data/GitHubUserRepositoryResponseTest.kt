package com.example.androidgithubsearch.data

import com.example.androidgithubsearch.data.api.GitHubUserRepositoryResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.mockkStatic
import org.junit.Test
import java.util.Date

class GitHubUserRepositoryResponseTest {
    private val response = GitHubUserRepositoryResponse(
        id = 1,
        name = "test_repo",
        url = "https://github.com/test/test_repo",
        owner = GitHubUserRepositoryResponse.Owner("https://github.com/test/avatar.jpg"),
        created = "2024-07-05T12:00:00Z",
        updated = "2024-07-05T12:30:00Z",
        language = "Kotlin",
        star = 100
    )

    @Test
    fun `toUserRepositoryEntity_正しく変換されること`() {
        mockkStatic(GitHubUserRepositoryResponse::class)
        val entity = response.toUserRepositoryEntity()
        entity.also {
            assertThat(it.id).isEqualTo(response.id)
            assertThat(it.name).isEqualTo(response.name)
            assertThat(it.url).isEqualTo(response.url)
            assertThat(it.avatar).isEqualTo(response.owner.avatar)
            assertThat(it.created).isInstanceOf(Date::class.java)
            assertThat(it.updated).isInstanceOf(Date::class.java)
            assertThat(it.language).isEqualTo(response.language)
            assertThat(it.star).isEqualTo(response.star)
        }
    }
}