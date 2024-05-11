package com.example.androidgithubsearch.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.repository.GitHubRepository
import com.example.androidgithubsearch.ui.adapter.RepositoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchRepositoryFragmentViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {
    private var _searchRepositories: MutableLiveData<List<RepositoryItem>> = MutableLiveData()
    val searchRepositories: LiveData<List<RepositoryItem>> = _searchRepositories

    private val _isRepositoryListVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRepositoryListVisible: LiveData<Boolean> = _isRepositoryListVisible

    private val searchQuery: MutableLiveData<String> = MutableLiveData()

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(1)
    val currentPage: LiveData<Int> = _currentPage

    fun clickNextPage() {
        _currentPage.value = _currentPage.value?.plus(1)
        searchRepository()
    }

    fun clickPreviousPage() {
        _currentPage.value = _currentPage.value?.minus(1)
        searchRepository()
    }

    fun clickSearchButton(query: String) {
        searchQuery.value = query
        _currentPage.value = 1
        searchRepository()
    }

    private fun searchRepository() {
        viewModelScope.launch {
            val result = searchQuery.value?.let {
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
                        _isRepositoryListVisible.value = false
                    }
                    return@launch
                }

                val repositoryItems: List<RepositoryItem> = repositoryList.map {
                    it.toRepositoryItem()
                }

                withContext(Dispatchers.Main) {
                    _searchRepositories.value = repositoryItems
                    _isRepositoryListVisible.value = true
                }
            }
        }
    }
}
