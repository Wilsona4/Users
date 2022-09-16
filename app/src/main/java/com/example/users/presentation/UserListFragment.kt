package com.example.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.users.R
import com.example.users.data.model.User
import com.example.users.databinding.FragmentUserListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : Fragment(), UserListAdapter.Interaction {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: MainActivityViewModel by activityViewModels()
    lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userListAdapter = UserListAdapter(this)
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userListAdapter
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.uiState.collect {
                        // Update UI elements
                        binding.progress.isVisible = it.loading
                        userListAdapter.submitList(it.students)
                    }
                }

                launch {
                    userViewModel.uiEvent.collect {
                        Snackbar.make(binding.rvUser, it, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(position: Int, item: User) {
        val action = UserListFragmentDirections.actionUserListFragmentToUserDetailsFragment(item)
        findNavController().navigate(action)
    }
}