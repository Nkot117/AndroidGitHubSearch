package com.example.androidgithubsearch.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.database.entity.UserRepositoryEntity
import com.example.androidgithubsearch.repository.GitHubRepository
import com.example.androidgithubsearch.ui.adapter.RepositoryItem
import com.example.androidgithubsearch.util.SharedPreferencesKeys
import com.example.androidgithubsearch.util.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserRepositoryFragmentViewModel @Inject constructor(
        val gitHubRepository: GitHubRepository,
        private val preferencesUtil: SharedPreferencesUtil
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
    private val _avatarUrl: MutableLiveData<String?> = MutableLiveData(null)
    val avatarUrl: LiveData<String?> = _avatarUrl

    // アカウント設定ボタンタップ
    private val _showAccountSettingDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    val showAccountSettingDialog: LiveData<Boolean> = _showAccountSettingDialog

    // リポジトリリスト表示
    private val _isRepositoryListVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    val isRepositoryListVisible: LiveData<Boolean> = _isRepositoryListVisible

    // ローディングの状態
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        _accountName.value = getUsername()
        viewModelScope.launch(Dispatchers.IO) {
            getUserRepositories()
        }
    }

    fun fetchAndLoadUserRepositories(username: String) {
        _isLoading.value = true
        setUsername(username)
        viewModelScope.launch(Dispatchers.IO) {
            gitHubRepository.fetchAndSaveUserRepositories(getUsername())
            getUserRepositories()
        }
    }

    fun onAccountSettingButtonClicked() {
        _showAccountSettingDialog.value = true
    }

    fun showAccountSettingDialogComplete() {
        _showAccountSettingDialog.value = false
    }

    private fun setUsername(username: String) {
        _accountName.value = username
        preferencesUtil.savePref(SharedPreferencesKeys.USER_NAME, username)
    }

    private fun getUsername(): String {
        return preferencesUtil.getPref(SharedPreferencesKeys.USER_NAME) ?: ""
    }

    private suspend fun getUserRepositories() {
        val result = gitHubRepository.getUserRepositories()

        if (result.isSuccess) {
            val repositoryList: List<UserRepositoryEntity>? = result.getOrNull()

            if (repositoryList.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    _repositoryCount.value = "0 Repositories"
                    _avatarUrl.value = null
                    _userRepositories.value = emptyList()
                    _isRepositoryListVisible.value = false
                    _isLoading.value = false
                }
                return
            }

            // RepositoryItemに変換
            repositoryList.let { list ->
                val repositoryItems = list.map {
                    it.toRepositoryItem()
                }

                // UIスレッドでLiveDataを更新
                withContext(Dispatchers.Main) {
                    _repositoryCount.value = "${repositoryList.size} Repositories"
                    _avatarUrl.value = repositoryList[0].avatar
                    _userRepositories.value = repositoryItems
                    _isRepositoryListVisible.value = true
                    _isLoading.value = false
                }
            }
        }
    }
}
