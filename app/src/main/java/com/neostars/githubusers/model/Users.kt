package com.neostars.githubusers.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val avatar: Int,
    val name: String,
    val username: String,
    val location: String,
    val repository: Int,
    val company: String
): Parcelable
