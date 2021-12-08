package com.example.flrs

import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Query("SELECT * FROM Foods")
    fun getAll():List<RowModel>

    @Insert
    fun insert(foods: Foods)
    @Update
    fun update(foods: Foods)
    @Delete
    fun delete(foods: Foods)
}