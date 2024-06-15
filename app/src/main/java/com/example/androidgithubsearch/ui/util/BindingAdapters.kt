package com.example.androidgithubsearch.ui.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidgithubsearch.R
import com.example.androidgithubsearch.ui.adapter.favoriterpositoryadapter.FavoriteRepositoryAdapter
import com.example.androidgithubsearch.ui.adapter.favoriterpositoryadapter.FavoriteRepositoryItem
import com.example.androidgithubsearch.ui.adapter.searchrepositoryadapter.SearchRepositoryAdapter
import com.example.androidgithubsearch.ui.adapter.searchrepositoryadapter.SearchRepositoryItem
import com.example.androidgithubsearch.ui.adapter.userrepositoryadapter.UserRepositoryAdapter
import com.example.androidgithubsearch.ui.adapter.userrepositoryadapter.UserRepositoryItem


object BindingAdapters {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setAvatarUrl(imageView: ImageView, url: String?) {
        if(url == null) {
            imageView.load(R.drawable.question_mark_24) {
                crossfade(true)
            }
        } else {
            imageView.load(url) {
                crossfade(true)
            }
        }

    }
    
    @BindingAdapter("userRepositoryListData")
    @JvmStatic
    fun setUserRepositoryListData(recyclerView: RecyclerView, data: List<UserRepositoryItem>?) {
        val adapter = recyclerView.adapter as UserRepositoryAdapter
        adapter.submitList(data)
    }

    @BindingAdapter("searchRepositoryListData")
    @JvmStatic
    fun setSearchRepositoryListData(recyclerView: RecyclerView, data: List<SearchRepositoryItem>?) {
        val adapter = recyclerView.adapter as SearchRepositoryAdapter
        adapter.submitList(data)
    }

    @BindingAdapter("favoriteRepositoryListData")
    @JvmStatic
    fun setFavoriteRepositoryListData(recyclerView: RecyclerView, data: List<FavoriteRepositoryItem>?) {
        val adapter = recyclerView.adapter as FavoriteRepositoryAdapter
        adapter.submitList(data)
    }

    @BindingAdapter("setFavoriteIcon")
    @JvmStatic
    fun setFavoriteIcon(imageView: ImageView, isFavorite: Boolean) {
        if(isFavorite) {
            imageView.setImageResource(R.drawable.favorite_24px)
        } else {
            imageView.setImageResource(R.drawable.heart_plus_24px)
        }
    }
}
