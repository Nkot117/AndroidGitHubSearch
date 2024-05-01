package com.example.androidgithubsearch.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidgithubsearch.R
import com.example.androidgithubsearch.ui.adapter.RepositoryAdapter
import com.example.androidgithubsearch.ui.adapter.RepositoryItem


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
    
    @BindingAdapter("listData")
    @JvmStatic
    fun setListData(recyclerView: RecyclerView, data: List<RepositoryItem>?) {
        val adapter = recyclerView.adapter as RepositoryAdapter
        adapter.submitList(data)
    }
}
