package com.neostars.githubusers.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.neostars.githubusers.R
import com.neostars.githubusers.model.Users

class DetailUser : AppCompatActivity() {
    companion object {
        var EXTRA_DATA = "extra_data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setStatusBarGradient(this)
        setContentView(R.layout.activity_detail_user)

        val setAvatar: ImageView = findViewById(R.id.set_avatar)
        val setFullName: TextView = findViewById(R.id.set_fullname)
        val setUsername:TextView = findViewById(R.id.set_username)
        val setLocation:TextView = findViewById(R.id.set_location)
        val setRepository:TextView = findViewById(R.id.set_repository)
        val setCompany:TextView = findViewById(R.id.set_company)
        val setFollowers: TextView = findViewById(R.id.set_followers)
        val setFollowing: TextView = findViewById(R.id.set_following)

        val listUser = intent.getParcelableExtra<Users>(EXTRA_DATA)
        if (listUser != null) {
            setAvatar.setImageResource(listUser.avatar)
            setFullName.text = listUser.fullname
            setUsername.text = listUser.username
            setLocation.text = listUser.location
            setRepository.text = listUser.repository
            setCompany.text = listUser.company
            setFollowers.text = listUser.followers
            setFollowing.text = listUser.following
        }
    }

    fun setStatusBarGradient(activity: Activity){
        val window: Window = activity.window
        val background = ContextCompat.getDrawable(activity, R.drawable.backgroundgradient)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
        window.setBackgroundDrawable(background)
    }
}