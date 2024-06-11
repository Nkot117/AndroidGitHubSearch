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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FavoriteRepositoryFragmentViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
) : ViewModel() {
    private val _favoriteRepositoryList: MutableLiveData<List<FavoriteRepositoryEntity>> =
        MutableLiveData()
    val favoriteRepositoryList: LiveData<List<FavoriteRepositoryEntity>> = _favoriteRepositoryList

    init {
        viewModelScope.launch {
            val result = gitHubRepository.getFavoriteRepositories()
            if (result.isSuccess) {
                val data = result.getOrNull()
                val favoriteRepositoryList = data ?: emptyList()
                _favoriteRepositoryList.value = favoriteRepositoryList
                Log.d("FavoriteRepositoryFragmentViewModel", "favoriteRepositoryList: $favoriteRepositoryList")
            }
        }
    }
}
