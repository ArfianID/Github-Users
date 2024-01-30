package com.arfian.githubuser.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arfian.githubuser.data.UserRepository
import com.arfian.githubuser.di.Injection
import com.arfian.githubuser.viewmodel.DetailViewModel
import com.arfian.githubuser.viewmodel.FavoriteViewModel
import com.arfian.githubuser.viewmodel.FollowersViewModel
import com.arfian.githubuser.viewmodel.FollowingViewModel
import com.arfian.githubuser.viewmodel.MainViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            //DetailViewModel
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(userRepository) as T
            }

            //FavoriteViewModel
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(userRepository) as T
            }

            //FollowersViewModel
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                FollowersViewModel(userRepository) as T
            }

            //FollowingViewModel
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                FollowingViewModel(userRepository) as T
            }

            //MainViewModel
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            val repository = Injection.provideRepository(context)
            return instance ?: synchronized(this) {
                Log.d("ViewModelFactory", "Creating ViewModelFactory with repository: $repository")
                instance ?: ViewModelFactory(repository)
            }.also { instance = it }
        }
    }
}
