package com.bignerdranch.android.pract7.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details")
data class DetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val typeId: Int,
    val weight: Double,
    val material: String,
    val diameter: Double
)
