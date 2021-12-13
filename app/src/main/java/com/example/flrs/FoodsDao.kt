package com.example.flrs

import androidx.room.*
import androidx.room.Dao

@Dao
interface FoodsDao {
    @Query("SELECT food_id, food_name,register_date,period FROM Foods,Categories WHERE Foods.category_id=Categories.category_id")
    fun getAll():List<RowModel>
      @Insert
      fun insert(foods: Foods)
      @Update
      fun update(foods: Foods)
    @Query("DELETE FROM foods WHERE food_id in (:food_id) ")
    fun delete(food_id:Int)
}



