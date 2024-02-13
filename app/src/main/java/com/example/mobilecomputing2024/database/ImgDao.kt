package com.example.mobilecomputing2024.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilecomputing2024.entity.ImgEntity


@Dao
interface ImgDao {
    @Query("SELECT * FROM images ORDER BY id DESC LIMIT 1")
    fun getLatestImg(): ImgEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImg(img: ImgEntity)

}