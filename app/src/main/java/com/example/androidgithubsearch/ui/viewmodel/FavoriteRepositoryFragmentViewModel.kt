package com.example.androidgithubsearch.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidgithubsearch.data.api.GitHubSearchRepositoryResponse
import com.example.androidgithubsearch.data.database.entity.FavoriteRepositoryEntity
import com.example.androidgithubsearch.data.repository.GitHubRepository
import com.example.androidgithubsearch.ui.adapter.favoriterpositoryadapter.FavoriteRepositoryItem
import com.example.androidgithubsearch.ui.adapter.searchrepositoryadapter.SearchRepositoryItem
import com.example.androidgithubsearch.utils.dateStringToDate
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
    private val _favoriteRepositoryList: MutableLiveData<List<FavoriteRepositoryItem>> =
        MutableLiveData()
    val favoriteRepositoryList: LiveData<List<FavoriteRepositoryItem>> = _favoriteRepositoryList

    private val _moveUrlPage = MutableLiveData<String?>()
    val moveUrlPage: LiveData<String?> = _moveUrlPage

    init {
        viewModelScope.launch {
            val result = gitHubRepository.getFavoriteRepositories()
            if (result.isSuccess) {
                val data = result.getOrNull()
                val favoriteRepositoryList = data ?: emptyList()
                _favoriteRepositoryList.value = favoriteRepositoryList.map {
                    FavoriteRepositoryItem(
                        id = it.id,
                        name = it.name,
                        url = it.url,
                        created = it.created.dateStringToDate(),
                        updated = it.updated.dateStringToDate(),
                        language = it.language,
                        star = it.star,
                        avatar = it.avatar,
                        isFavorite = true,
                        clickAddFavoriteAction = {
                            viewModelScope.launch {
                                gitHubRepository.addFavoriteRepository(
                                    FavoriteRepositoryEntity(
                                        id = it.id,
                                        name = it.name,
                                        url = it.url,
                                        created = it.created,
                                        updated = it.updated,
                                        language = it.language,
                                        star = it.star,
                                        avatar = it.avatar
                                    )
                                )
                            }
                        },
                        clickRemoveFavoriteAction = {
                            viewModelScope.launch {
                             gitHubRepository.deleteFavoriteRepository(
                                    FavoriteRepositoryEntity(
                                        id = it.id,
                                        name = it.name,
                                        url = it.url,
                                        created = it.created,
                                        updated = it.updated,
                                        language = it.language,
                                        star = it.star,
                                        avatar = it.avatar
                                    )
                             )
                            }
                        },
                        clickItemAction = {
                            _moveUrlPage.value = it.url
                        }
                    )
                }
                Log.d(
                    "FavoriteRepositoryFragmentViewModel",
                    "favoriteRepositoryList: $favoriteRepositoryList"
                )
            }
        }
    }

    fun moveDonePage() {
        _moveUrlPage.value = null
    }
}
