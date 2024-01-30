package com.arfian.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arfian.githubuser.data.local.entity.UserEntity
import com.arfian.githubuser.databinding.UserItemBinding
import com.bumptech.glide.Glide

class FavoriteListAdapter :
    ListAdapter<UserEntity, FavoriteListAdapter.ViewHolder>(DiffCallback) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteUser = getItem(position)
        holder.bind(favoriteUser)
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(favoriteUser) }
    }

    inner class ViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: UserEntity) {
            binding.apply {
                Glide.with(itemView)
                    .load(favoriteUser.avatar)
                    .centerCrop()
                    .into(profileImage)
                tvUsername.text = favoriteUser.username
                tvLink.text = favoriteUser.htmlUrl
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(favoriteUser: UserEntity)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
