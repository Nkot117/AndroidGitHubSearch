package com.example.androidgithubsearch.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.androidgithubsearch.R
import com.example.androidgithubsearch.databinding.FragmentUserRepositoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserRepositoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserRepositoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    
    private var _binding: FragmentUserRepositoryBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserRepositoryBinding.inflate(inflater, container, false)
        binding.btnSearchRepository.setOnClickListener {
            moveToSearchRepositoryFragment()
        }
        binding.btnFavoriteRepository.setOnClickListener {
            moveToFavoriteRepositoryFragment()
        }
        return binding.root
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    
    private fun moveToSearchRepositoryFragment() {
        findNavController().navigate(R.id.action_userRepositoryFragment_to_searchRepositoryFragment)

    }
    
    private fun moveToFavoriteRepositoryFragment() {
        findNavController().navigate(R.id.action_userRepositoryFragment_to_favoriteRepositoryFragment)
    }
    
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserRepositoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserRepositoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}