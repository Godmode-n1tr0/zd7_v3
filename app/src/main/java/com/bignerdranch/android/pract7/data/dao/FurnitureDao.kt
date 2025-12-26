package com.bignerdranch.android.pract7.data.dao

import androidx.room.*
import com.bignerdranch.android.pract7.data.entities.FurnitureItemEntity
import com.bignerdranch.android.pract7.data.entities.FurnitureTypeEntity

@Dao
interface FurnitureDao {

    @Query("SELECT * FROM furniture_types")
    fun getAllTypes(): List<FurnitureTypeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<FurnitureTypeEntity>)

    @Insert
    fun insert(item: FurnitureTypeEntity)

    @Update
    fun update(item: FurnitureTypeEntity)

    @Delete
    fun delete(item: FurnitureTypeEntity)

    @Query("""
        SELECT * FROM furniture_types
        WHERE name LIKE '%' || :query || '%'
    """)
    fun search(query: String): List<FurnitureTypeEntity>

    // Methods for FurnitureItemEntity
    @Insert
    suspend fun insertItem(item: FurnitureItemEntity)

    @Update
    suspend fun updateItem(item: FurnitureItemEntity)

    @Delete
    suspend fun deleteItem(item: FurnitureItemEntity)

    @Query("SELECT * FROM furniture_items WHERE typeId = :typeId")
    suspend fun getItemsForType(typeId: Int): List<FurnitureItemEntity>
}
