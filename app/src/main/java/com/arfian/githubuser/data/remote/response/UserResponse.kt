package com.arfian.githubuser.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class User(

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<UserItem>
)

@Parcelize
data class UserItem(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("bio")
	val bio: String,

	@field:SerializedName("public_repos")
	val publicRepos: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("organizations_url")
	val organizationsUrl: String,
) : Parcelable
