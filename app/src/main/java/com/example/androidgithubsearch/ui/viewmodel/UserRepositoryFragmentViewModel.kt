package com.example.androidgithubsearch.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.database.entity.UserRepositoryEntity
import com.example.androidgithubsearch.repository.GitHubRepository
import com.example.androidgithubsearch.ui.adapter.RepositoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRepositoryFragmentViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {
    private val _userRepositories = MutableStateFlow<List<RepositoryItem>>(emptyList())
    val userRepositories: StateFlow<List<RepositoryItem>> = _userRepositories
    
    fun fetchAndLoadUserRepositories(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            gitHubRepository.fetchAndSaveUserRepositories(username)
            getUserRepositories()
        }
    }
    
    private suspend fun getUserRepositories() {
        val result = gitHubRepository.getUserRepositories()
        if (result.isSuccess) {
            val repositoryList: List<UserRepositoryEntity>? = result.getOrNull()
            repositoryList?.let { list ->
                val repositoryItems = list.map { RepositoryItem(it.name) }
                _userRepositories.value = repositoryItems
            }
        }
    }
}