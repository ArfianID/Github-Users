package com.arfian.githubuser.viewmodel

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arfian.githubuser.ui.settings.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingPreferences: SettingPreferences) : ViewModel() {
    fun getDarkMode(): LiveData<Boolean> = settingPreferences.getDarkMode().asLiveData()

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveDarkMode(isDarkMode)
        }
    }
}

