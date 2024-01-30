package com.arfian.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.UserRepository
import com.arfian.githubuser.data.local.entity.UserEntity
import com.arfian.githubuser.data.remote.response.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _userDetail = MutableLiveData<Result<UserItem>>()
    val userDetail: LiveData<Result<UserItem>> = _userDetail

    val isFavorite = MutableLiveData<Boolean>()

    fun getUserDetail(username: String) {
        _userDetail.value = Result.Loading

        viewModelScope.launch {
            val result = userRepository.getUserDetail(username)
            _userDetail.value = result
        }
    }

    fun checkFavorite(username: String) {
        viewModelScope.launch {
            val favorite = userRepository.getFavoriteByUsername(username)
            isFavorite.value = favorite != null
            Log.d("DetailViewModel", "checkFavorite: username: $username, isFavorite: ${isFavorite.value}")
        }
    }

    fun toggleFavorite(username: String, avatar: String?, htmlUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingFavorite = userRepository.getFavoriteByUsername(username)
            if (existingFavorite != null) {
                userRepository.delete(existingFavorite)
                isFavorite.postValue(false)
            } else {
                userRepository.insert(UserEntity(username, avatar, htmlUrl))
                isFavorite.postValue(true)
            }
        }
    }
}