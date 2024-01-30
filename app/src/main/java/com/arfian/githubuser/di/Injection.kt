package com.arfian.githubuser.di

import android.content.Context
import com.arfian.githubuser.data.UserRepository
import com.arfian.githubuser.data.local.room.UserDatabase
import com.arfian.githubuser.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getDatabase(context)
        val dao = database.userDao()

        return UserRepository.getInstance(apiService, dao)
    }
}