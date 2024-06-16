package com.example.androidgithubsearch.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.repository.GitHubRepository
import com.example.androidgithubsearch.ui.adapter.searchrepositoryadapter.SearchRepositoryItem
import com.example.androidgithubsearch.utils.dateStringToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRepositoryFragmentViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {
    private var _searchRepositories: MutableLiveData<List<SearchRepositoryItem>> = MutableLiveData()
    val searchRepositories: LiveData<List<SearchRepositoryItem>> = _searchRepositories
    private val _currentPage: MutableLiveData<Int> = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    private val _moveUrlPage: MutableLiveData<String?> = MutableLiveData()
    val moveUrlPage: LiveData<String?> = _moveUrlPage

    private var searchQuery: String? = null

    private val favoriteRepository: Flow<List<FavoriteRepositoryEntity>> = flow {
        val result = gitHubRepository.getFavoriteRepositories()
        if (result.isSuccess) {
            result.getOrNull()?.collect {
                emit(it)
            }
        }
    }

    private var searchResult: List<GitHubSearchRepositoryResponse.Item>? = null

    fun clickNextPage() {
        _currentPage.value = _currentPage.value?.plus(1)
        searchRepository()
    }

    fun clickPreviousPage() {
        _currentPage.value = _currentPage.value?.minus(1)
        searchRepository()
    }

    fun clickSearchButton(query: String?) {
        query?.let {
            searchQuery = it
            _currentPage.value = 1
            searchRepository()
        }
    }

    fun moveDonePage() {
        _moveUrlPage.value = null
    }

    private fun searchRepository() {
        viewModelScope.launch {
            getSearchRepositories()
            updateSearchRepositories()
        }
    }

    private fun createSearchRepositoryItem(
        repositoryResponse: GitHubSearchRepositoryResponse.Item,
        isFavorite: Boolean
    ): SearchRepositoryItem {
        return SearchRepositoryItem(
            id = repositoryResponse.id,
            name = repositoryResponse.name,
            url = repositoryResponse.url,
            created = repositoryResponse.created.dateStringToDate(),
            updated = repositoryResponse.updated.dateStringToDate(),
            language = repositoryResponse.language ?: "Unknown",
            star = repositoryResponse.star,
            avatar = repositoryResponse.owner.avatar,
            isFavorite = isFavorite,
            clickAddFavoriteAction = {
                viewModelScope.launch {
                    gitHubRepository.addFavoriteRepository(
                        createFavoriteRepositoryEntity(
                            repositoryResponse
                        )
                    )
                }
            },
            clickRemoveFavoriteAction = {
                viewModelScope.launch {
                    gitHubRepository.deleteFavoriteRepository(
                        createFavoriteRepositoryEntity(
                            repositoryResponse
                        )
                    )
                }
            },
            clickItemAction = {
                _moveUrlPage.value = repositoryResponse.url
            }
        )
    }

    private fun createFavoriteRepositoryEntity(repositoryResponse: GitHubSearchRepositoryResponse.Item): FavoriteRepositoryEntity {
        return FavoriteRepositoryEntity(
            id = repositoryResponse.id,
            name = repositoryResponse.name,
            url = repositoryResponse.url,
            avatar = repositoryResponse.owner.avatar,
            created = repositoryResponse.created,
            updated = repositoryResponse.updated,
            language = repositoryResponse.language ?: "Unknown",
            star = repositoryResponse.star
        )
    }

    private suspend fun getSearchRepositories() {
        val result = searchQuery?.let {
            gitHubRepository.searchRepositories(it, _currentPage.value ?: 1)
        }

        if (result?.isSuccess == true) {
            val data = result.getOrNull()
            searchResult = data?.items ?: emptyList()
        } else {
            searchResult = emptyList()
        }
    }

    private suspend fun updateSearchRepositories() {
        favoriteRepository.collect { favoriteRepositoryList ->
            searchResult?.let { searchResult ->
                val favoriteRepositoryIdList = favoriteRepositoryList.map { it.id }
                val repositoryItems = searchResult.map {
                    createSearchRepositoryItem(
                        it,
                        favoriteRepositoryIdList.contains(it.id)
                    )
                }
                _searchRepositories.value = repositoryItems
            } ?: run {
                _searchRepositories.value = emptyList()
            }
        }
    }
}
