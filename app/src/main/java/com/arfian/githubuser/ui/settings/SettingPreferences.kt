package com.arfian.githubuser.ui.settings

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        @Volatile
        private var instance: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences =
            instance ?: synchronized(this) {
                instance ?: SettingPreferences(dataStore).apply {
                    instance = this
                }
            }

        // Define a key for the dark mode preference
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    suspend fun saveDarkMode(isDarkMode: Boolean) {
        dataStore.edit { settings ->
            settings[DARK_MODE_KEY] = isDarkMode
        }
        Log.d("SettingPreferences", "Dark mode saved: $isDarkMode")
    }

    suspend fun getDarkMode(): Boolean {
        val preferences = dataStore.data.first()
        val isDarkMode = preferences[DARK_MODE_KEY] ?: false
        Log.d("SettingPreferences", "Dark mode retrieved: $isDarkMode")
        return isDarkMode
    }
}