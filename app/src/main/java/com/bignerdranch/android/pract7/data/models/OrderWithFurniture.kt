package com.bignerdranch.android.pract7.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bignerdranch.android.pract7.data.entities.FurnitureTypeEntity
import com.bignerdranch.android.pract7.data.entities.OrderDetailCrossRef
import com.bignerdranch.android.pract7.data.entities.OrderEntity

data class OrderWithFurniture(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "orderNumber",
        entityColumn = "id",
        associateBy = Junction(
            value = OrderDetailCrossRef::class,
            parentColumn = "orderNumber",
            entityColumn = "furnitureId"
        )
    )
    val furniture: List<FurnitureTypeEntity>
)
