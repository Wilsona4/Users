package com.example.users.data.repository

import androidx.room.withTransaction
import com.example.users.data.local.UserDatabase
import com.example.users.data.model.User
import com.example.users.data.remote.ApiService
import com.example.users.di.Dispatcher
import com.example.users.di.MyDispatchers.IO
import com.example.users.domain.repository.IUserRepository
import com.example.users.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDatabase: UserDatabase,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : IUserRepository {

    override fun getUsers(): Flow<Resource<List<User>>> = flow {
        val userDao = userDatabase.userDao()

        emit(Resource.Loading())

        val cachedData = userDao.readUsers().first()

        if (cachedData.isNotEmpty()) {
            emit(Resource.Success(cachedData))
        }

        val remoteData = try {
            apiService.getUsers()
        } catch (e: Exception) {
            userDao.readUsers().collect { userList ->
                emit(Resource.Error(message = e.message ?: "Something went wrong", userList))
            }
            null
        }

        remoteData?.let { studentList ->
            userDatabase.withTransaction {
//                userDao.deleteUsers()
                studentList.forEach { student ->
                    userDao.addStudent(student)
                }
            }
            userDao.readUsers().collect {
                emit(Resource.Success(it))
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun saveImage(id: Int, imagePath: String) = withContext(ioDispatcher) {
        userDatabase.userDao().updateImage(id, imagePath)
    }

    override suspend fun addUser(user: User) = withContext(ioDispatcher) {
        userDatabase.userDao().addStudent(user)
    }

}

