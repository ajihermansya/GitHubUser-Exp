package com.rumahsosial.gituse.detail.local

import android.content.Context
import androidx.room.Room


class DbModule(private val context: Context) {
    private val db = Room.databaseBuilder(context, AppDb::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .build()

    val userDao = db.userDao()
}