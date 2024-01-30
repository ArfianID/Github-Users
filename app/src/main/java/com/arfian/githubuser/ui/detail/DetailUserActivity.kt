package com.arfian.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.arfian.githubuser.R
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.remote.response.UserItem
import com.arfian.githubuser.databinding.ActivityDetailUserBinding
import com.arfian.githubuser.utils.ViewModelFactory
import com.arfian.githubuser.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private var username: String? = null
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @StringRes val TAB_TITLES = intArrayOf(
            R.string.followers_tab,
            R.string.following_tab
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailViewModel.getUserDetail(username!!)
            detailViewModel.checkFavorite(username!!)
        }

        initViews()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_share -> {
                detailViewModel.userDetail.value?.let {
                    if (it is Result.Success) {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT, "${resources.getString(R.string.share_text)}  \n${it.data.htmlUrl}")
                        }
                        startActivity(Intent.createChooser(shareIntent, "Share link using"))
                    } else {
                        showError(getString(R.string.data_not_available))
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        setupToolbar()
        setupViewPager()
    }

    private fun setupObservers() {
        detailViewModel.userDetail.observe(this) { result: Result<UserItem> ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    parseUserDetail(result.data)
                    binding.fabFavorite.setOnClickListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            detailViewModel.toggleFavorite(result.data.login, result.data.avatarUrl, result.data.htmlUrl)
                            showFavoriteStatusToast(detailViewModel.isFavorite.value!!)
                        }
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                }
            }
        }

        detailViewModel.isFavorite.observe(this) { isFavorite: Boolean ->
            updateFavoriteButton(isFavorite)
        }
    }

    private fun setupViewPager() {
        username?.let {
            val bundle = Bundle().apply {
                putString(EXTRA_USERNAME, it)
            }
            val sectionsPagerAdapter = SectionPagerAdapter(this, bundle)
            binding.viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.detail_user)
        }
    }

    private fun parseUserDetail(userItem: UserItem) {
        with(binding) {
            tvName.text = userItem.name
            tvUsername.text = userItem.login
            tvFollowersValue.text = userItem.followers.toString()
            tvFollowingValue.text = userItem.following.toString()
            tvRepositoryValue.text = userItem.publicRepos
            tvBio.text = userItem.bio
            Glide.with(this@DetailUserActivity)
                .load(userItem.avatarUrl)
                .centerCrop()
                .into(ivProfilePhoto)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        binding.fabFavorite.setImageResource(iconRes)
    }

    private fun showFavoriteStatusToast(isFavorite: Boolean) {
        val toastMessage = if (isFavorite) {
            resources.getString(R.string.deleted_from_favorite)
        } else resources.getString(R.string.added_to_favorite)
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}