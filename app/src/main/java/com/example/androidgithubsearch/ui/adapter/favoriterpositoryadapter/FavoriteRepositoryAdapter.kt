package com.example.androidgithubsearch.ui.adapter.favoriterpositoryadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.androidgithubsearch.databinding.FavoriteRepositoryRowItemBinding

class FavoriteRepositoryAdapter :
    ListAdapter<FavoriteRepositoryItem, FavoriteRepositoryAdapter.RepositoryItemViewHolder>(
        DIFF_UTIL_ITEM_CALLBACK
    ) {
    class RepositoryItemViewHolder(
        private val binding: FavoriteRepositoryRowItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryItem: FavoriteRepositoryItem) {
//            binding.repositoryItem = repositoryItem
//
//            binding.root.setOnClickListener {
//                repositoryItem.clickItemAction()
//            }
//
//            binding.addFavorite.setOnClickListener {
//                binding.removeFavorite.visibility = View.VISIBLE
//                binding.addFavorite.visibility = View.GONE
//                repositoryItem.clickAddFavoriteAction()
//            }
//
//            binding.removeFavorite.setOnClickListener {
//                binding.removeFavorite.visibility = View.GONE
//                binding.addFavorite.visibility = View.VISIBLE
//                repositoryItem.clickRemoveFavoriteAction()
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view =
            FavoriteRepositoryRowItemBinding.inflate(
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
        val DIFF_UTIL_ITEM_CALLBACK = object : DiffUtil.ItemCallback<FavoriteRepositoryItem>() {
            override fun areItemsTheSame(
                oldItem: FavoriteRepositoryItem,
                newItem: FavoriteRepositoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: FavoriteRepositoryItem,
                newItem: FavoriteRepositoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
