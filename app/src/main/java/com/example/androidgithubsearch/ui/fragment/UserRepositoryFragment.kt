package com.example.androidgithubsearch.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.androidgithubsearch.R
import com.example.androidgithubsearch.databinding.FragmentUserRepositoryBinding
import com.example.androidgithubsearch.ui.activity.WebViewActivity
import com.example.androidgithubsearch.ui.adapter.userrepositoryadapter.UserRepositoryAdapter
import com.example.androidgithubsearch.ui.viewmodel.UserRepositoryFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        setRepositoryRecyclerView()
        viewModel.showAccountSettingDialog.observe(viewLifecycleOwner) {
            if (it) {
                showAccountSettingDialog()
                viewModel.showAccountSettingDialogComplete()
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.accountName.value?.let {
                binding.swipeRefreshLayout.isRefreshing = false
                viewModel.fetchAndLoadUserRepositories(it)
            } ?: return@setOnRefreshListener
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setRepositoryRecyclerView() {
        val adapter = UserRepositoryAdapter()
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

    private fun showAccountSettingDialog() {
        val dialogLayout = layoutInflater.inflate(R.layout.accout_setting_dialog, null, false)
        val editText = dialogLayout.findViewById<EditText>(R.id.editTextDialog)
        editText.setText(viewModel.accountName.value)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Account Setting")
            .setView(dialogLayout)
            .setPositiveButton("OK") { _, _ ->
                val inputAccountName = editText.text.toString()
                if (inputAccountName.isNotBlank()) {
                    viewModel.fetchAndLoadUserRepositories(inputAccountName)
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()
        dialog.show()
    }
}
