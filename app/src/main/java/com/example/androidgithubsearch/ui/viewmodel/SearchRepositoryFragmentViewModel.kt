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

    private val searchQuery: MutableLiveData<String> = MutableLiveData()

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    fun searchRepository(query: String) {
        // 同じ文言で何回も検索しないようにする
        if (searchQuery.value == query) {
            return
        }

        searchQuery.value = query

        viewModelScope.launch {
            val result = gitHubRepository.searchRepositories(query, _currentPage.value?.plus(1) ?: 1)

            if (result.isSuccess) {
                val data = result.getOrNull() ?: return@launch

                val totalCount = data.totalCount
                Log.d("SearchRepositoryFragmentViewModel", "totalCount: $totalCount")

                val repositoryList = data.items

                if (repositoryList.isEmpty()) {
                    return@launch
                }

                val repositoryItems: List<RepositoryItem> = repositoryList.map {
                    it.toRepositoryItem()
                }

                withContext(Dispatchers.Main) {
                    _searchRepositories.value = repositoryItems
                }
            }
        }
    }
}
