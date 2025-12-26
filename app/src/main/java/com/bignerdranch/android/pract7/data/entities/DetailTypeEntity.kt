package com.bignerdranch.android.pract7.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_types")
data class DetailTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val specifications: String, // Characteristics like weight, material, diameter
    val category: String // For grouping (e.g., nuts, bolts, screws)
)
