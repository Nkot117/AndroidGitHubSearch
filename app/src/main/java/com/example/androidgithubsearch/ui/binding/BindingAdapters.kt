package com.example.androidgithubsearch.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load


object BindingAdapters {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun setAvatarUrl(imageView: ImageView, url: String?) {
        imageView.load(url) {
            crossfade(true)
        }
    }
}
