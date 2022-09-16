package com.example.users.data.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val city: String,
    @Embedded(prefix = "geo")
    val geo: Geo? = null,
    val street: String,
    val suite: String,
    val zipcode: String
) : Parcelable