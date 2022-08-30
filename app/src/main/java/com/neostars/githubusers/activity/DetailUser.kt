package com.neostars.githubusers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neostars.githubusers.R
import com.neostars.githubusers.model.Users

class DetailUser : AppCompatActivity() {
    companion object {
        var EXTRA_DATA = "extra_data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val listUse = intent.getParcelableExtra<Users>(EXTRA_DATA)


    }
}