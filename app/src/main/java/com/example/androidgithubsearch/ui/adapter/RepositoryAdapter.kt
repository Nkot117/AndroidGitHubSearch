package com.example.androidgithubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgithubsearch.databinding.RepositoryRowItemBinding
import com.example.androidgithubsearch.repository.GitHubRepository
import com.example.androidgithubsearch.ui.activity.WebViewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RepositoryAdapter(
    private val coroutineScope: CoroutineScope,
    private val gitHubRepository: GitHubRepository
) : ListAdapter<RepositoryItem, RepositoryAdapter.RepositoryItemViewHolder>(DIFF_UTIL_ITEM_CALLBACK) {
    class RepositoryItemViewHolder(
        private val binding: RepositoryRowItemBinding,
        private val coroutineScope: CoroutineScope,
        private val gitHubRepository: GitHubRepository
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryItem: RepositoryItem) {
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
            RepositoryRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(view, coroutineScope, gitHubRepository)
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_UTIL_ITEM_CALLBACK = object : DiffUtil.ItemCallback<RepositoryItem>() {
            override fun areItemsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
