package com.example.flrs

import androidx.room.*
import androidx.room.Dao

@Dao
interface FoodsDao {
    @Query("SELECT food_name,register_date,null as limit_date FROM Foods,Categories WHERE Foods.category_id=Categories.category_id")
    fun getAll():List<RowModel>

    @Insert
    fun insert(foods: Foods)
    @Update
    fun update(foods: Foods)
    @Delete
    fun delete(foods: Foods)
}