package com.neostars.githubusers.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neostars.githubusers.R
import com.neostars.githubusers.adapter.UserListAdapter
import com.neostars.githubusers.model.Users

class MainActivity : AppCompatActivity() {
    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUser = findViewById(R.id.rv_listUser)
        rvUser.setHasFixedSize(true)
        list.addAll(listUsers)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = UserListAdapter(list)
        rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallBack{
            override fun onItemClicked(data: Users) {
                val intent = Intent(this@MainActivity, DetailUser::class.java)
                intent.putExtra(DetailUser.EXTRA_DATA, data)
                startActivity(intent)
            }
        })
    }

    private val listUsers: ArrayList<Users>
        @SuppressLint("ResourceType")
        get() {
            val avatar = resources.obtainTypedArray(R.array.avatar)
            val fullName = resources.getStringArray(R.array.full_name)
            val username = resources.getStringArray(R.string.user_name)
            val location = resources.getStringArray(R.string.location)
            val repository = resources.getStringArray(R.array.repository)
            val company = resources.getStringArray(R.array.company)
            val followers = resources.getStringArray(R.array.followers)
            val following = resources.getStringArray(R.array.following)

            val listData = ArrayList<Users>()

            for (i in username.indices) {
                val user = Users(
                    avatar.getResourceId(i, -1),
                    fullName[i],
                    username[i],
                    location[i],
                    repository[i],
                    company[i],
                    followers[i],
                    following[i]
                )
                listData.add(user)
            }
            return listData
        }
}