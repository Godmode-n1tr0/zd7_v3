package com.bignerdranch.android.pract7.data.dao

import androidx.room.*
import com.bignerdranch.android.pract7.data.entities.SupplierEntity

@Dao
interface SupplierDao {

    @Query("SELECT * FROM suppliers")
    fun getAll(): List<SupplierEntity>

    @Insert
    fun insert(item: SupplierEntity)

    @Update
    fun update(item: SupplierEntity)

    @Delete
    fun delete(item: SupplierEntity)
}
