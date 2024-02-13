package com.example.mobilecomputing2024.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilecomputing2024.entity.ImgEntity
import com.example.mobilecomputing2024.entity.UserEntity

@Database(
    entities = [UserEntity::class, ImgEntity::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun imgDao(): ImgDao
}