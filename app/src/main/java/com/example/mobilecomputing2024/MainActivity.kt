package com.example.mobilecomputing2024

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.mobilecomputing2024.database.AppDatabase
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme



class MainActivity : ComponentActivity() {
    companion object {
        lateinit var database: AppDatabase
            private set
        fun showNotification(context: Context, message: String) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, "channel id")
                .setContentText(message)
                .setContentTitle("Notification")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(1, notification)
        }
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
        setContent {
            Mobilecomputing2024Theme{
                MobileComputingNavHost()
            }
        }
    }
    override fun onStop() {
        super.onStop()
        val serviceIntent = Intent(this, GyroscopeService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
