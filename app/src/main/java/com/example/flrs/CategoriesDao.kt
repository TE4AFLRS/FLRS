package com.example.flrs

import androidx.room.*
import androidx.room.Dao

@Dao
interface CategoriesDao {
    @Query("SELECT category_id,category_name FROM Categories")
    fun getAll():List<CategoryRow>

//    @Insert
//    fun insert(category: Categories)
//    @Update
//    fun update(category: Categories)
//    @Delete
//    fun delete(category: Categories)
}