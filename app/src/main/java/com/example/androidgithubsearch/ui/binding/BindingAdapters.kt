package com.example.androidgithubsearch.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidgithubsearch.ui.adapter.RepositoryAdapter
import com.example.androidgithubsearch.ui.adapter.RepositoryItem


object BindingAdapters {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun setAvatarUrl(imageView: ImageView, url: String?) {
        imageView.load(url) {
            crossfade(true)
        }
    }
    
    @BindingAdapter("app:listData")
    @JvmStatic
    fun setListData(recyclerView: RecyclerView, data: List<RepositoryItem>?) {
        val adapter = recyclerView.adapter as RepositoryAdapter
        adapter.submitList(data)
    }
}