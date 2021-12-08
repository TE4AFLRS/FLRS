package com.example.flrs

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Categories(
    @PrimaryKey(autoGenerate = true)
    val category_id: Int,

    var category_name: String,

    var period: String
)


