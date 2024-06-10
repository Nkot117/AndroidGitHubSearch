package com.example.androidgithubsearch.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.androidgithubsearch.databinding.FragmentSearchRepositoryBinding
import com.example.androidgithubsearch.ui.activity.WebViewActivity
import com.example.androidgithubsearch.ui.adapter.searchrepositoryadapter.SearchRepositoryAdapter
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
        setRepositoryRecyclerView()
        setSearchView()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setRepositoryRecyclerView() {
        val adapter = SearchRepositoryAdapter()
        binding.repositoryRecyclerView.also {
            it.adapter = adapter
            it.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        viewModel.moveUrlPage.observe(viewLifecycleOwner) { url ->
            url?.let {
                val intent = WebViewActivity.createIntent(binding.root.context, it)
                binding.root.context.startActivity(intent)
            }
        }
    }

    private fun setSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.clickSearchButton(query)

                // キーボードを閉じる
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(binding.searchView.windowToken, 0)

                // フォーカスを外す
                binding.searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}
