package com.neostars.githubusers.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.neostars.githubusers.R
import com.neostars.githubusers.activity.DetailUser
import com.neostars.githubusers.model.Users

class UserListAdapter(private val userList: ArrayList<Users>): RecyclerView.Adapter<UserListAdapter.UserViewViewHolder>() {
    class UserViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView = itemView.findViewById(R.id.profile_image)
        var tvFullName: TextView = itemView.findViewById(R.id.full_name)
        var tvNickname: TextView = itemView.findViewById(R.id.username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_users, parent, false)
        return UserViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewViewHolder, position: Int) {
        val user = userList[position]

        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(55, 55))
            .into(holder.avatar)

        holder.tvFullName.text = user.fullname
        holder.tvNickname.text = user.username
    }
}