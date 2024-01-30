package com.arfian.githubuser.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arfian.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteUser: UserEntity)

    @Delete
    suspend fun delete(favoriteUser: UserEntity)

    @Query("SELECT * FROM favorite ORDER BY username ASC")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM favorite WHERE username = :username")
    suspend fun getFavoriteByUsername(username: String): UserEntity?
}