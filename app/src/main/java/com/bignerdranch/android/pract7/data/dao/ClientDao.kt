package com.bignerdranch.android.pract7.data.dao

import androidx.room.*
import com.bignerdranch.android.pract7.data.entities.ClientEntity

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients")
    fun getAll(): List<ClientEntity>

    @Insert
    fun insert(item: ClientEntity)

    @Update
    fun update(item: ClientEntity)

    @Delete
    fun delete(item: ClientEntity)
}
