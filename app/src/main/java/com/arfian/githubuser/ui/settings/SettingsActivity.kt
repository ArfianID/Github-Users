package com.arfian.githubuser.ui.settings

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.arfian.githubuser.R
import com.arfian.githubuser.databinding.ActivitySettingsBinding
import com.arfian.githubuser.utils.SettingsViewModelFactory
import com.arfian.githubuser.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewModel()
        observeDarkMode()
        setDarkMode()
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.settings)
        }
    }

    private fun setupViewModel() {
        val pref = SettingPreferences.getInstance(applicationContext.dataStore)
        viewModel = ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingsViewModel::class.java]
    }

    private fun observeDarkMode() {
        viewModel.getDarkMode().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.darkmodeText.text = getString(R.string.dark_mode)
                binding.darkmodeSwitch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.darkmodeText.text = getString(R.string.light_mode)
                binding.darkmodeSwitch.isChecked = false
            }
        }
    }

    private fun setDarkMode() {
        binding.darkmodeSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean->
            viewModel.setDarkMode(isChecked)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}