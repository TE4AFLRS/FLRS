package com.example.flrs

import androidx.room.*
import androidx.room.Dao

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM Categories")
    fun getAll():List<RowModel>

    @Insert
    fun insert(category: Categories)
    @Update
    fun update(category: Categories)
    @Delete
    fun delete(category: Categories)
}