package com.example.users.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.users.data.local.dao.UserDao
import com.example.users.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        var USER_DATABASE = "user_database"
    }
}