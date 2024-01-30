package com.arfian.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.UserRepository
import com.arfian.githubuser.data.remote.response.UserItem
import kotlinx.coroutines.launch

class FollowingViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _userFollowing = MutableLiveData<Result<List<UserItem>>>()
    val userFollowing: LiveData<Result<List<UserItem>>> = _userFollowing

    fun setFollowing(username: String) {
        _userFollowing.value = Result.Loading

        viewModelScope.launch {
            val result = userRepository.getUserFollowing(username)
            _userFollowing.value = result
        }
    }
}