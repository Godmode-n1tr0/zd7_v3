package com.bignerdranch.android.pract7.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["furnitureId", "detailId"])
data class FurnitureDetailCrossRef(
    val furnitureId: Int,
    val detailId: Int
)
