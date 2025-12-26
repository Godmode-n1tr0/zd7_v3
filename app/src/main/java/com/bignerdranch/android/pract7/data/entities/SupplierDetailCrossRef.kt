package com.bignerdranch.android.pract7.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["supplierId", "detailId"])
data class SupplierDetailCrossRef(
    val supplierId: Int,
    val detailId: Int
)
