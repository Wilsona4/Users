package com.example.users.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @Embedded(prefix = "address")
    val address: Address? = null,
    @Embedded(prefix = "company")
    val company: Company,
    val email: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val username: String,
    val website: String,
    val imageUri: String? = null
) : Parcelable