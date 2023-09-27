package com.rumahsosial.gituse.detail.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rumahsosial.gituse.model.ResponseUserGithubs
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ResponseUserGithubs.Item)

    @Query("SELECT * FROM user")
    fun loadAll(): LiveData<MutableList<ResponseUserGithubs.Item>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1 ")
    fun findById(id : Int):ResponseUserGithubs.Item

    @Delete
    fun delete(ser: ResponseUserGithubs.Item)
}