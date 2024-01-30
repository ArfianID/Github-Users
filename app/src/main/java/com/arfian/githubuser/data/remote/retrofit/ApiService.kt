package com.arfian.githubuser.data.remote.retrofit

import com.arfian.githubuser.data.remote.response.User
import com.arfian.githubuser.data.remote.response.UserItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(NetworkConstants.SEARCH_USERS)
    suspend fun getSearchUser(@Query("q") query: String): User

    @GET(NetworkConstants.USER_DETAIL)
    suspend fun getDetailUser(@Path("username") username: String): UserItem

    @GET(NetworkConstants.USER_FOLLOWERS)
    suspend fun getUserFollowers(@Path("username") username: String): List<UserItem>

    @GET(NetworkConstants.USER_FOLLOWING)
    suspend fun getUserFollowing(@Path("username") username: String): List<UserItem>
}