package com.rumahsosial.gituse.detail.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rumahsosial.gituse.model.ResponseUserGithubs

@Database(entities = [ResponseUserGithubs.Item::class], version = 1, exportSchema = false)
abstract class AppDb:RoomDatabase() {
    abstract fun userDao() : UserDao

}