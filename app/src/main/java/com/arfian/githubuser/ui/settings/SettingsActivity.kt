package com.arfian.githubuser.ui.settings

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arfian.githubuser.R
import com.arfian.githubuser.databinding.ActivitySettingsBinding
import com.arfian.githubuser.utils.SettingsViewModelFactory
import com.arfian.githubuser.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingPreferences.getInstance(applicationContext.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        // Observe dark mode
        viewModel.isDarkMode.observe(this) { isDarkMode ->
            if (isDarkMode) {
                binding.darkmodeSwitch.isChecked = isDarkMode
                binding.darkmodeText.text = getString(R.string.dark_mode)
            } else {
                binding.darkmodeSwitch.isChecked = isDarkMode
                binding.darkmodeText.text = getString(R.string.light_mode)
            }
        }

        // Set dark mode
        binding.darkmodeSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            viewModel.setDarkMode(this, isChecked)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.settings)
        }
    }
}