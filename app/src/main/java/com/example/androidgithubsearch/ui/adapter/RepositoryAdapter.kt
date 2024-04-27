package com.example.androidgithubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgithubsearch.databinding.RepositoryRowItemBinding


class RepositoryAdapter :
    ListAdapter<RepositoryItem, RepositoryAdapter.RepositoryItemViewHolder>(DIFF_UTIL_ITEM_CALLBACK) {
    class RepositoryItemViewHolder(
        private val binding: RepositoryRowItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryItem: RepositoryItem) {
            binding.repositoryNameTextView.text = repositoryItem.name
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view =
            RepositoryRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(view)
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