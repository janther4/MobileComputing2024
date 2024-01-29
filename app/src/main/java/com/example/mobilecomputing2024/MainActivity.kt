package com.example.mobilecomputing2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mobilecomputing2024Theme{
                MobileComputingNavHost()
            }
        }
    }
}

