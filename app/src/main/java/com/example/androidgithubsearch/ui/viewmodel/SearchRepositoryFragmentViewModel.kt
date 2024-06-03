package com.example.androidgithubsearch.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.repository.GitHubRepository
import com.example.androidgithubsearch.ui.activity.WebViewActivity
import com.example.androidgithubsearch.ui.adapter.SearchRepositoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchRepositoryFragmentViewModel @Inject constructor(
    val gitHubRepository: GitHubRepository,
) : ViewModel() {
    private var _searchRepositories: MutableLiveData<List<SearchRepositoryItem>> = MutableLiveData()
    val searchRepositories: LiveData<List<SearchRepositoryItem>> = _searchRepositories
    private val _currentPage: MutableLiveData<Int> = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

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

    fun clickAddFavoriteButton(repositoryItem: SearchRepositoryItem) {
        viewModelScope.launch {
            gitHubRepository.addFavoriteRepository(repositoryItem.toFavoriteRepositoryEntity())
        }
        repositoryItem.isFavorite = true
    }

    fun clickRemoveFavoriteButton(repositoryItem: SearchRepositoryItem) {
        viewModelScope.launch {
            gitHubRepository.deleteFavoriteRepository(repositoryItem.toFavoriteRepositoryEntity())
        }
        repositoryItem.isFavorite = false
    }


        fun clickRepositoryItem(context: Context, repositoryItem: SearchRepositoryItem) {
        val intent = WebViewActivity.createIntent(context, repositoryItem.url)
        context.startActivity(intent)
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
                    it.toSearchRepositoryItem(favoriteRepositoryIdList)
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
}
