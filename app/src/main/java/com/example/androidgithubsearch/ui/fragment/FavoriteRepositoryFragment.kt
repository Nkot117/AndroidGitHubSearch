package com.example.androidgithubsearch.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.androidgithubsearch.databinding.FragmentFavoriteRepositoryBinding
import com.example.androidgithubsearch.ui.viewmodel.FavoriteRepositoryFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRepositoryFragment : Fragment() {
    private var _binding: FragmentFavoriteRepositoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteRepositoryFragmentViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRepositoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}