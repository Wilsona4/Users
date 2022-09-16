package com.example.users.domain.repository

import com.example.users.data.model.User
import com.example.users.util.Resource
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUsers(): Flow<Resource<List<User>>>
    suspend fun saveImage(id: Int, imagePath: String)
    suspend fun addUser(user: User)
}