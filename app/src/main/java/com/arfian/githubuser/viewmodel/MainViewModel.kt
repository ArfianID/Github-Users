package com.arfian.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.UserRepository
import com.arfian.githubuser.data.remote.response.UserItem
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _listUser = MutableLiveData<Result<List<UserItem>>>()
    val listUser: LiveData<Result<List<UserItem>>> = _listUser

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun searchUsers(query: String) {
        _listUser.value = Result.Loading

        viewModelScope.launch {
            try {
                val result = userRepository.searchUsers(query)
                _listUser.value = result
            } catch (e: Exception) {
                _listUser.value = Result.Error(e)
                Log.e(TAG, "Exception: ${e.message}")
            }
        }
    }
}
