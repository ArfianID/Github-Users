package com.arfian.githubuser.data

import android.util.Log
import com.arfian.githubuser.data.local.entity.UserEntity
import com.arfian.githubuser.data.local.room.UserDao
import com.arfian.githubuser.data.remote.response.UserItem
import com.arfian.githubuser.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {

    suspend fun searchUsers(query: String): Result<List<UserItem>> {
        return try {
            val response = apiService.getSearchUser(query)
            val users = response.items
            Log.d("UserRepository", "Search successful. Users: $users")
            Result.Success(users)
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}")
            Result.Error(e)
        }
    }

    suspend fun getUserDetail(username: String): Result<UserItem> {
        return try {
            val user = apiService.getDetailUser(username)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getUserFollowers(username: String): Result<List<UserItem>> {
        return try {
            val users = apiService.getUserFollowers(username)
            Result.Success(users)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getUserFollowing(username: String): Result<List<UserItem>> {
        return try {
            val users = apiService.getUserFollowing(username)
            Result.Success(users)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getAllUsers(): List<UserEntity> {
        return userDao.getAllUsers()
    }

    suspend fun getFavoriteByUsername(username: String): UserEntity? {
        return userDao.getFavoriteByUsername(username)
    }

    suspend fun insert(user: UserEntity) = userDao.insert(user)
    suspend fun delete(user: UserEntity) = userDao.delete(user)

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, userDao: UserDao): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao).apply { instance = this }
            }
    }
}