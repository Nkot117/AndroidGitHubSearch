package com.example.androidgithubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgithubsearch.databinding.SearchRepositoryRowItemBinding
import com.example.androidgithubsearch.ui.activity.WebViewActivity
import com.example.androidgithubsearch.ui.viewmodel.SearchRepositoryFragmentViewModel

class SearchRepositoryAdapter(
    private val viewModel: SearchRepositoryFragmentViewModel
) : ListAdapter<SearchRepositoryItem, SearchRepositoryAdapter.RepositoryItemViewHolder>(
    DIFF_UTIL_ITEM_CALLBACK
) {
    class RepositoryItemViewHolder(
        private val binding: SearchRepositoryRowItemBinding,
        private val viewModel: SearchRepositoryFragmentViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryItem: SearchRepositoryItem) {
            binding.repositoryItem = repositoryItem

            binding.root.setOnClickListener { view ->
                viewModel.clickRepositoryItem(view.context, repositoryItem)
            }

            binding.addFavorite.setOnClickListener {
                binding.removeFavorite.visibility = View.VISIBLE
                binding.addFavorite.visibility = View.GONE
                repositoryItem.isFavorite = true
                viewModel.clickAddFavoriteButton(repositoryItem)
            }

            binding.removeFavorite.setOnClickListener {
                binding.removeFavorite.visibility = View.GONE
                binding.addFavorite.visibility = View.VISIBLE
                repositoryItem.isFavorite = false
                viewModel.clickRemoveFavoriteButton(repositoryItem)
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
        return RepositoryItemViewHolder(view, viewModel)
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
