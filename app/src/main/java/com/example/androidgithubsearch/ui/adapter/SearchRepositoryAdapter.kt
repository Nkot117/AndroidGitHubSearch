package com.example.androidgithubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgithubsearch.databinding.SearchRepositoryRowItemBinding
import com.example.androidgithubsearch.repository.GitHubRepository
import com.example.androidgithubsearch.ui.activity.WebViewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SearchRepositoryAdapter(
    private val coroutineScope: CoroutineScope,
    private val gitHubRepository: GitHubRepository
) : ListAdapter<SearchRepositoryItem, SearchRepositoryAdapter.RepositoryItemViewHolder>(DIFF_UTIL_ITEM_CALLBACK) {
    class RepositoryItemViewHolder(
        private val binding: SearchRepositoryRowItemBinding,
        private val coroutineScope: CoroutineScope,
        private val gitHubRepository: GitHubRepository
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryItem: SearchRepositoryItem) {
            binding.repositoryItem = repositoryItem

            binding.root.setOnClickListener { view ->
                val intent = WebViewActivity.createIntent(view.context, repositoryItem.url)
                view.context.startActivity(intent)
            }

            binding.addFavorite.setOnClickListener {
                binding.removeFavorite.visibility = View.VISIBLE
                binding.addFavorite.visibility = View.GONE
                repositoryItem.isFavorite = true
                coroutineScope.launch {
                    gitHubRepository.addFavoriteRepository(repositoryItem.toFavoriteRepositoryEntity())
                }
            }

            binding.removeFavorite.setOnClickListener {
                binding.removeFavorite.visibility = View.GONE
                binding.addFavorite.visibility = View.VISIBLE
                repositoryItem.isFavorite = false
                coroutineScope.launch {
                    gitHubRepository.deleteFavoriteRepository(repositoryItem.toFavoriteRepositoryEntity())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view =
            SearchRepositoryRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(view, coroutineScope, gitHubRepository)
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
