package com.example.androidgithubsearch.ui.fragment.userrepository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgithubsearch.databinding.UserRepositoryRowItemBinding

class UserRepositoryAdapter(private val viewModel: UserRepositoryFragmentViewModel) :
    ListAdapter<UserRepositoryItem, UserRepositoryAdapter.RepositoryItemViewHolder>(
        DIFF_UTIL_ITEM_CALLBACK
    ) {
    class RepositoryItemViewHolder(
        private val binding: UserRepositoryRowItemBinding,
        private val viewModel: UserRepositoryFragmentViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryItem: UserRepositoryItem) {
            binding.repositoryItem = repositoryItem
            binding.root.setOnClickListener {
                viewModel.clickRepositoryItem(repositoryItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view =
            UserRepositoryRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_UTIL_ITEM_CALLBACK = object : DiffUtil.ItemCallback<UserRepositoryItem>() {
            override fun areItemsTheSame(
                oldItem: UserRepositoryItem,
                newItem: UserRepositoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: UserRepositoryItem,
                newItem: UserRepositoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
