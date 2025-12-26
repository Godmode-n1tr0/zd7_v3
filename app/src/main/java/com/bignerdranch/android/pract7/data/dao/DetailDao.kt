package com.bignerdranch.android.pract7.data.dao

import androidx.room.*
import com.bignerdranch.android.pract7.data.entities.DetailTypeEntity

@Dao
interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detailType: DetailTypeEntity)

    @Update
    suspend fun update(detailType: DetailTypeEntity)

    @Delete
    suspend fun delete(detailType: DetailTypeEntity)

    @Query("SELECT * FROM detail_types")
    suspend fun getAllDetailTypes(): List<DetailTypeEntity>

    @Query("SELECT * FROM detail_types WHERE category = :category")
    suspend fun getDetailTypesByCategory(category: String): List<DetailTypeEntity>
}
