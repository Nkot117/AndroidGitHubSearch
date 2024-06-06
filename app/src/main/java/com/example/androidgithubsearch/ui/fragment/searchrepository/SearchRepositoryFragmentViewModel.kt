package com.example.androidgithubsearch.ui.fragment.searchrepository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchRepositoryFragmentViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {
    private var _searchRepositories: MutableLiveData<List<SearchRepositoryItem>> = MutableLiveData()
    val searchRepositories: LiveData<List<SearchRepositoryItem>> = _searchRepositories
    private val _currentPage: MutableLiveData<Int> = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    private val _moveUrlPage: MutableLiveData<String> = MutableLiveData()
    val moveUrlPage: LiveData<String> = _moveUrlPage

    private var searchQuery: String? = null

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

    private fun searchRepository() {
        viewModelScope.launch {
            val result = searchQuery?.let {
                gitHubRepository.searchRepositories(it, _currentPage.value ?: 1)
            } ?: return@launch

            if (result.isSuccess) {
                val data = result.getOrNull() ?: return@launch

                val totalCount = data.totalCount
                Log.d("SearchRepositoryFragmentViewModel", "totalCount: $totalCount")

                val repositoryList = data.items

                if (repositoryList.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        _searchRepositories.value = emptyList()
                    }
                    return@launch
                }

                val favoriteRepositoriesResult = gitHubRepository.getFavoriteRepositories()
                val favoriteRepositoryIdList = if (favoriteRepositoriesResult.isSuccess) {
                    favoriteRepositoriesResult.getOrNull()?.map { it.id } ?: emptyList()
                } else {
                    emptyList()
                }

                val repositoryItems: List<SearchRepositoryItem> = repositoryList.map {
                    createSearchRepositoryItem(
                        it,
                        favoriteRepositoryIdList.contains(it.id)
                    )
                }

                withContext(Dispatchers.Main) {
                    _searchRepositories.value = repositoryItems
                }
            } else {
                Log.e(
                    "SearchRepositoryFragmentViewModel",
                    "searchRepositories error",
                    result.exceptionOrNull()
                )
                withContext(Dispatchers.Main) {
                    _searchRepositories.value = emptyList()
                }
            }
        }
    }

    private fun dateStringToDate(dateString: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return formatter.parse(dateString)
    }

    private fun createSearchRepositoryItem(repositoryResponse: GitHubSearchRepositoryResponse.Item, isFavorite: Boolean): SearchRepositoryItem {
        return SearchRepositoryItem(
            id = repositoryResponse.id,
            name = repositoryResponse.name,
            url = repositoryResponse.url,
            created = dateStringToDate(repositoryResponse.created),
            updated = dateStringToDate(repositoryResponse.updated),
            language = repositoryResponse.language ?: "Unknown",
            star = repositoryResponse.star,
            avatar = repositoryResponse.owner.avatar,
            isFavorite = isFavorite,
            clickAddFavoriteAction = {
                viewModelScope.launch {
                    gitHubRepository.addFavoriteRepository(
                        FavoriteRepositoryEntity(
                            id = repositoryResponse.id,
                            name = repositoryResponse.name,
                            url = repositoryResponse.url,
                        )
                    )
                }
            },
            clickRemoveFavoriteAction = {
                viewModelScope.launch {
                    gitHubRepository.deleteFavoriteRepository(
                        FavoriteRepositoryEntity(
                            id = repositoryResponse.id,
                            name = repositoryResponse.name,
                            url = repositoryResponse.url,
                        )
                    )
                }
            },
            clickItemAction = {
                _moveUrlPage.value = repositoryResponse.url
            }
        )
    }
}
