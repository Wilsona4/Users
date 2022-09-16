package com.example.users.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: MyDispatchers)

enum class MyDispatchers {
    DEFAULT, IO, MAIN, UNCONFINED
}