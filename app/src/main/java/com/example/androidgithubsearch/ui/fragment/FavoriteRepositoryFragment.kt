package com.example.androidgithubsearch.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.androidgithubsearch.databinding.FragmentFavoriteRepositoryBinding
import com.example.androidgithubsearch.ui.viewmodel.FavoriteRepositoryFragmentViewModel

class FavoriteRepositoryFragment : Fragment() {
    private var _binding: FragmentFavoriteRepositoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteRepositoryFragmentViewModel>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}