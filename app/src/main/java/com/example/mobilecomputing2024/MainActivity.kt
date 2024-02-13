package com.example.mobilecomputing2024

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.mobilecomputing2024.database.AppDatabase
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme


class MainActivity : ComponentActivity() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        ).fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()
        Log.d("Database", "Database initialized")

        setContent {
            Mobilecomputing2024Theme{
                MobileComputingNavHost()
            }
        }
    }
}
