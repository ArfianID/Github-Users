package com.neostars.githubusers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.neostars.githubusers.R
import com.neostars.githubusers.model.Users

class DetailUser : AppCompatActivity() {
    companion object {
        var EXTRA_DATA = "extra_data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
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
}