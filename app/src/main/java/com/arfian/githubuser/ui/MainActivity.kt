package com.arfian.githubuser.ui

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfian.githubuser.R
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.remote.response.UserItem
import com.arfian.githubuser.databinding.ActivityMainBinding
import com.arfian.githubuser.ui.adapter.UserAdapter
import com.arfian.githubuser.ui.detail.DetailUserActivity
import com.arfian.githubuser.ui.settings.SettingPreferences
import com.arfian.githubuser.ui.settings.SettingsActivity
import com.arfian.githubuser.ui.settings.dataStore
import com.arfian.githubuser.utils.SettingsViewModelFactory
import com.arfian.githubuser.utils.ViewModelFactory
import com.arfian.githubuser.viewmodel.MainViewModel
import com.arfian.githubuser.viewmodel.SettingsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingPreferences.getInstance(applicationContext.dataStore))
    }
    private lateinit var userAdapter: UserAdapter


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Observe dark mode
        observeDarkMode()
        // Initialize RecyclerView and Adapter
        setupRecyclerView()
        // Searching in search view
        startSearch()
        // Observing user
        mainViewModel.listUser.observe(this) { handleResult(it) }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                startActivity(Intent(this, FavoriteListActivity::class.java))
                true
            }

            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager(this@MainActivity).orientation
                )
            )
            adapter = userAdapter

            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: UserItem) {
                    Intent(this@MainActivity, DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
                        startActivity(this)
                    }
                }
            })
        }
    }

    private fun startSearch() {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager

        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard(this@MainActivity)
                if (!query.isNullOrBlank()) {
                    Log.d("SearchView", "Query submitted: $query")
                    mainViewModel.searchUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun handleResult(result: Result<List<UserItem>>) {
        when (result) {
            is Result.Loading -> {
                showLoading(true)
            }
            is Result.Success -> {
                showLoading(false)
                updateData(result.data)
            }
            is Result.Error -> {
                showLoading(false)
                showErrorMessage(result.exception.message)
            }
        }
    }

    private fun updateData(users: List<UserItem>) {
        userAdapter.submitList(users)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(it: String?) {
        if (!it.isNullOrEmpty()) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun observeDarkMode() {
        settingsViewModel.isDarkMode.observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Log.d("MainActivity", "Dark mode applied: $isDarkMode")
        }
    }
}
