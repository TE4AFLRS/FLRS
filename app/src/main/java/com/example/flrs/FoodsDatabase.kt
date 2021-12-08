package com.example.flrs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Foods::class,Categories::class],version = 1)
abstract class FoodsDatabase:RoomDatabase() {
    abstract fun foodsDao():FoodsDao
    abstract fun categoriesDao():CategoriesDao
    companion object {
        private var INSTANCE: FoodsDatabase? = null
        private val look = Any()
        fun getInstance(context: Context): FoodsDatabase {
            synchronized(look) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FoodsDatabase::class.java,
                        "Foods.db"
                    ).allowMainThreadQueries().build()
                }
                return INSTANCE!!
            }
        }
    }

}


