package com.example.androidgithubsearch.ui.adapter.searchrepositoryadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgithubsearch.databinding.SearchRepositoryRowItemBinding

class SearchRepositoryAdapter :
    ListAdapter<SearchRepositoryItem, SearchRepositoryAdapter.RepositoryItemViewHolder>(
        DIFF_UTIL_ITEM_CALLBACK
    ) {
    class RepositoryItemViewHolder(
        private val binding: SearchRepositoryRowItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryItem: SearchRepositoryItem) {
            binding.repositoryItem = repositoryItem

            binding.root.setOnClickListener {
                repositoryItem.clickItemAction()
            }

            binding.addFavorite.setOnClickListener {
                repositoryItem.clickAddFavoriteAction()
            }

            binding.removeFavorite.setOnClickListener {
                repositoryItem.clickRemoveFavoriteAction()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view =
            SearchRepositoryRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RepositoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_UTIL_ITEM_CALLBACK = object : DiffUtil.ItemCallback<SearchRepositoryItem>() {
            override fun areItemsTheSame(
                oldItem: SearchRepositoryItem,
                newItem: SearchRepositoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: SearchRepositoryItem,
                newItem: SearchRepositoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
