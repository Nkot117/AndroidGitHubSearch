package com.example.androidgithubsearch.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.androidgithubsearch.databinding.FragmentUserRepositoryBinding
import com.example.androidgithubsearch.ui.adapter.RepositoryAdapter
import com.example.androidgithubsearch.ui.viewmodel.UserRepositoryFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRepositoryFragment : Fragment() {
    private var _binding: FragmentUserRepositoryBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: UserRepositoryFragmentViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRepositoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.fetchAndLoadUserRepositories("Nkot117")
        setRepositoryRecyclerView()
        return binding.root
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    
    private fun setRepositoryRecyclerView() {
        val adapter = RepositoryAdapter()
        binding.repositoryRecyclerView.also {
            it.adapter = adapter
            it.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        
        lifecycleScope.launch {
            viewModel.userRepositories.collect {
                adapter.submitList(it)
            }
        }
    }
}
