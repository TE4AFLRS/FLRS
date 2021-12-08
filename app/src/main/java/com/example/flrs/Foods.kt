package com.example.flrs

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Foods",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Categories::class,
            parentColumns = arrayOf("category_id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Foods(
    @PrimaryKey(autoGenerate = true)
    val food_id: Int,

    var food_name: String,

    var category_id: Int,

    var register_date: String
)


