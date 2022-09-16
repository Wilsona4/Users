package com.example.users.data.local.dao

import androidx.room.*
import com.example.users.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    /*Add User to Database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudent(user: User)

    /*Get User in the Database*/
    @Transaction
    @Query("SELECT * FROM user_table")
    fun readUsers(): Flow<List<User>>

    /*Delete user in the Database*/
    @Query("DELETE FROM user_table")
    suspend fun deleteUsers()

    /*Save User Image*/
    @Query("UPDATE user_table SET imageUri=:imagePath WHERE id=:userId")
    suspend fun updateImage(userId: Int, imagePath: String)
}