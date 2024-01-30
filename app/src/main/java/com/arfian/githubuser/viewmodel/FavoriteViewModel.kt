package com.arfian.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfian.githubuser.data.Result
import com.arfian.githubuser.data.UserRepository
import com.arfian.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {
    private val _listUser = MutableLiveData<Result<List<UserEntity>>>()
    val listUser: LiveData<Result<List<UserEntity>>> = _listUser

    fun getFavoriteUsers() {
        _listUser.value = Result.Loading
        viewModelScope.launch {
            try {
                val users = repository.getAllUsers()
                _listUser.value = Result.Success(users)
                Log.d("FavoriteViewModel", "getFavoriteUsers: Success, users: $users")
            } catch (e: Exception) {
                _listUser.value = Result.Error(e)
                Log.e("FavoriteViewModel", "Error getting favorite users", e)
            }
        }
    }
}