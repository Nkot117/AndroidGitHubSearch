package com.example.androidgithubsearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRepositoryFragmentViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {
    
    private fun fetchAndSaveUserRepositories(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            gitHubRepository.fetchAndSaveUserRepositories(username)
        }
    }
    
    private fun getUserRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            gitHubRepository.getUserRepositories()
        }
    }
}