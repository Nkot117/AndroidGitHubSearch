package com.example.androidgithubsearch.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.androidgithubsearch.databinding.FragmentSearchRepositoryBinding
import com.example.androidgithubsearch.ui.adapter.RepositoryAdapter
import com.example.androidgithubsearch.ui.viewmodel.SearchRepositoryFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRepositoryFragment : Fragment() {
    private var _binding: FragmentSearchRepositoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchRepositoryFragmentViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRepositoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.searchRepository("android")
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
    }
}
