package com.example.androidgithubsearch.ui.viewmodel

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

    fun searchRepository(query: String) {
        viewModelScope.launch {
            val result = gitHubRepository.searchRepositories(query)

            if (result.isSuccess) {
                val data = result.getOrNull() ?: return@launch

                val totalCount = data.totalCount

                val repositoryList = data.items ?: emptyList()

                if (repositoryList.isNullOrEmpty()) {
                    return@launch
                }

                val repositoryItems: List<RepositoryItem> = repositoryList.map {
                    it.toRepositoryItem()
                }

                withContext(Dispatchers.Main){
                    _searchRepositories.value = repositoryItems
                }
            }
        }
    }
}
