package com.example.androidgithubsearch.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidgithubsearch.R
import com.example.androidgithubsearch.ui.adapter.SearchRepositoryAdapter
import com.example.androidgithubsearch.ui.adapter.SearchRepositoryItem
import com.example.androidgithubsearch.ui.adapter.UserRepositoryAdapter
import com.example.androidgithubsearch.ui.adapter.UserRepositoryItem


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
}
