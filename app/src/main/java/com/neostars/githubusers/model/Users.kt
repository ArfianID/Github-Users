package com.neostars.githubusers.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val avatar: Int,
    val fullname: String,
    val username: String,
    val location: String,
    val repository: Int,
    val company: String,
    val followers: String,
    val following: String
): Parcelable
