package com.arfian.githubuser.viewmodel

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfian.githubuser.ui.settings.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingPreferences: SettingPreferences) : ViewModel() {
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    init {
        viewModelScope.launch {
            _isDarkMode.value = settingPreferences.getDarkMode()
        }
    }

    fun setDarkMode(context: Context, isDarkMode: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveDarkMode(isDarkMode)
            applyDarkMode(context, isDarkMode)
            _isDarkMode.value = isDarkMode
            Log.d("SettingsViewModel", "Dark mode set: $isDarkMode")
        }
    }

    private fun applyDarkMode(context: Context, isDarkMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            uiModeManager.setApplicationNightMode(
                if (isDarkMode) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
            )
        } else {
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}