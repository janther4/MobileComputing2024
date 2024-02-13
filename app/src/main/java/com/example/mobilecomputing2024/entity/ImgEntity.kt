package com.example.mobilecomputing2024.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImgEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imgUri: String
)

