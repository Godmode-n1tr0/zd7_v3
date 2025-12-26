package com.bignerdranch.android.pract7.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furniture_types")
data class FurnitureTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageUrl: String
)
