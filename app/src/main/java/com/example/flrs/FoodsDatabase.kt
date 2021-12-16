package com.example.flrs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Foods::class,Categories::class],version = 2)
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
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val sql = "INSERT INTO 'Categories' VALUES " +
                                    "(1, '卵', 14)," +
                                    "(2, '精肉', 3)," +
                                    "(3, '鮮魚', 3)," +
                                    "(4, '牛乳', 2)"
                            db.execSQL(sql)
                        }
                    })
                        .allowMainThreadQueries().build()
                }
                return INSTANCE!!
            }
        }
    }

}


