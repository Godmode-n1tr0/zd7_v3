package com.bignerdranch.android.pract7.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["orderNumber", "furnitureId"])
data class OrderDetailCrossRef(
    val orderNumber: String,
    val furnitureId: Int
)
