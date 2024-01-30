package com.arfian.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfian.githubuser.R
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.local.entity.UserEntity
import com.arfian.githubuser.databinding.ActivityFavoriteListBinding
import com.arfian.githubuser.ui.adapter.FavoriteListAdapter
import com.arfian.githubuser.ui.detail.DetailUserActivity
import com.arfian.githubuser.utils.ViewModelFactory
import com.arfian.githubuser.viewmodel.FavoriteViewModel

class FavoriteListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteListBinding
    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupViewModelObservers()
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getFavoriteUsers()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_users_title)
    }

    private fun setupRecyclerView() {
        favoriteListAdapter = FavoriteListAdapter().apply {
            setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback {
                override fun onItemClicked(favoriteUser: UserEntity) {
                    val intent = Intent(this@FavoriteListActivity, DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USERNAME, favoriteUser.username)
                    }
                    startActivity(intent)
                }
            })
        }

        binding.rvFavoriteUsers.apply {
            layoutManager = LinearLayoutManager(this@FavoriteListActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@FavoriteListActivity,
                    LinearLayoutManager(this@FavoriteListActivity).orientation
                )
            )
            adapter = favoriteListAdapter
        }
    }

    private fun setupViewModelObservers() {
        favoriteViewModel.listUser.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    favoriteListAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    Result.Error(result.exception)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
