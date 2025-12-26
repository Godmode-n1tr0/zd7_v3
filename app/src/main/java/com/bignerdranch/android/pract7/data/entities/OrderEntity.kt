package com.bignerdranch.android.pract7.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderNumber: String,
    val clientId: Int
)
