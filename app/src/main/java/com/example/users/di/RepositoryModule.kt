package com.example.users.di

import com.example.users.data.repository.UserRepositoryImpl
import com.example.users.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(
        userRepository: UserRepositoryImpl
    ): IUserRepository
}