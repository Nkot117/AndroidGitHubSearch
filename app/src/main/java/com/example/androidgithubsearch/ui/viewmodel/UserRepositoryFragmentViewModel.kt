package com.example.androidgithubsearch.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserRepositoryFragmentViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {
    // リスト表示するリポジトリリスト
    private val _userRepositories: MutableLiveData<List<RepositoryItem>> = MutableLiveData()
    val userRepositories: LiveData<List<RepositoryItem>> = _userRepositories
    
    // アカウント名
    private val _accountName: MutableLiveData<String> = MutableLiveData("")
    val accountName: LiveData<String> = _accountName
    
    // リポジトリの数
    private val _repositoryCount: MutableLiveData<String> = MutableLiveData("")
    val repositoryCount: LiveData<String> = _repositoryCount
    
    // アバターのURL
    private val _avatarUrl: MutableLiveData<String> = MutableLiveData("")
    val avatarUrl: LiveData<String> = _avatarUrl
    
    fun fetchAndLoadUserRepositories(username: String) {
        _accountName.value = username
        viewModelScope.launch(Dispatchers.IO) {
            gitHubRepository.fetchAndSaveUserRepositories(username)
            getUserRepositories()
        }
    }
    
    private suspend fun getUserRepositories() {
        val result = gitHubRepository.getUserRepositories()
        
        if (result.isSuccess) {
            val repositoryList: List<UserRepositoryEntity>? = result.getOrNull()
            
            // RepositoryItemに変換
            repositoryList?.let { list ->
                val repositoryItems = list.map {
                    RepositoryItem(
                        name = it.name,
                        url = it.url,
                        created = it.created,
                        updated = it.updated,
                        language = it.language,
                        star = it.star,
                        avatar = it.avatar
                    )
                }
                
                // UIスレッドでLiveDataを更新
                withContext(Dispatchers.Main) {
                    _repositoryCount.value = "${repositoryList.size} Repositories"
                    _avatarUrl.value = repositoryList[0].avatar
                    _userRepositories.value = repositoryItems
                }
            }
        }
    }
}
