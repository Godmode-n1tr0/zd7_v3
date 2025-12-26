package com.bignerdranch.android.pract7.data.dao

import androidx.room.*
import com.bignerdranch.android.pract7.data.entities.OrderEntity
import com.bignerdranch.android.pract7.data.models.OrderWithFurniture

@Dao
interface OrderDao {

    @Transaction
    @Query("SELECT * FROM orders")
    suspend fun getOrdersWithFurniture(): List<OrderWithFurniture>

    @Transaction
    @Query("SELECT * FROM orders WHERE clientId = :clientId")
    suspend fun getOrdersForClient(clientId: Int): List<OrderWithFurniture>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity)

    @Update
    suspend fun update(order: OrderEntity)

    @Delete
    suspend fun delete(order: OrderEntity)
}
