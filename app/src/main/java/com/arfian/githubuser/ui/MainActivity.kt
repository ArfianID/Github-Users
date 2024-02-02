package com.arfian.githubuser.ui

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
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
import androidx.core.content.ContextCompat
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

        observeDarkMode()
        setupRecyclerView()
        startSearch()
        mainViewModel.listUser.observe(this) { handleResult(it) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        setIconColorBasedOnTheme(menu)
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
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager(this@MainActivity).orientation))
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
                if (result.data.isEmpty()) {
                    userAdapter.submitList(emptyList())
                    showPromptAndEmptyList(true)
                    Toast.makeText(this, resources.getString(R.string.no_user_data_found), Toast.LENGTH_SHORT).show()
                } else {
                    showPromptAndEmptyList(false)
                    userAdapter.submitList(result.data)
                }
            }
            is Result.Error -> {
                userAdapter.submitList(emptyList())
                showPromptAndEmptyList(true)
                showLoading(false)
                Toast.makeText(this, result.exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity.currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun observeDarkMode() {
        settingsViewModel.getDarkMode().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.ivGithubIcon.setImageResource(R.mipmap.ic_github_white)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.ivGithubIcon.setImageResource(R.mipmap.ic_github_dark)
            }
            Log.d("MainActivity", "Dark mode applied: $isDarkModeActive")
        }
    }

    private fun showPromptAndEmptyList(isVisible: Boolean) {
        binding.tvPromptMain.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.ivEmptyList.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setIconColorBasedOnTheme(menu: Menu?) {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                setIconColor(menu, android.R.color.white)
            }
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                setIconColor(menu, android.R.color.black)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setIconColor(menu: Menu?, color: Int) {
        val colorFilter = BlendModeColorFilter(ContextCompat.getColor(this, color), BlendMode.SRC_IN)
        menu?.findItem(R.id.action_favorite)?.icon?.colorFilter = colorFilter
        menu?.findItem(R.id.action_settings)?.icon?.colorFilter = colorFilter
    }
}