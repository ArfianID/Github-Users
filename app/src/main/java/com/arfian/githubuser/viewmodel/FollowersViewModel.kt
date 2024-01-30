package com.arfian.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.UserRepository
import com.arfian.githubuser.data.remote.response.UserItem
import kotlinx.coroutines.launch

class FollowersViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _userFollowers = MutableLiveData<Result<List<UserItem>>>()
    val userFollowers: LiveData<Result<List<UserItem>>> = _userFollowers

    fun setFollowers(username: String) {
        _userFollowers.value = Result.Loading

        viewModelScope.launch {
            val result = userRepository.getUserFollowers(username)
            _userFollowers.value = result
        }
    }
}