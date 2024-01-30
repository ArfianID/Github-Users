package com.arfian.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.remote.response.UserItem
import com.arfian.githubuser.databinding.FragmentFollowersBinding
import com.arfian.githubuser.ui.adapter.UserAdapter
import com.arfian.githubuser.utils.ViewModelFactory
import com.arfian.githubuser.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private val followersViewModel: FollowersViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val adapter by lazy { UserAdapter() }
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).orEmpty()

        _binding = FragmentFollowersBinding.bind(view)

        // Initialize RecyclerView
        setUpRecyclerView()

        // Observe ViewModel
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@FollowersFragment.adapter
        }

        // Set the OnItemClickCallback on the adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserItem) {
                val intent = Intent(context, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
                }
                startActivity(intent)
            }
        })
    }

    private fun observeViewModel() {
        followersViewModel.apply {
            setFollowers(username)
            userFollowers.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}