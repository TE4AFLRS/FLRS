package com.example.flrs

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var contextDelivery: Context
    private lateinit var manager: NotificationManager

    fun getContext():Context{
        return contextDelivery
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contextDelivery = applicationContext

        // Android 8 (Oreo) への対応
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel =
                NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Default channel"
            manager.createNotificationChannel(channel)
        }

//        contextDelivery = applicationContext

        //alarmManagerの実装
        val alarmManager =
            this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        // プッシュ通知を表示
        val intent = Intent(this, NotificationProcess::class.java)
        val contentIntent =PendingIntent.getBroadcast(this, 0, intent, 0)
        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show()
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+2000, contentIntent)

        //
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_notifications,
                R.id.navigation_page_register_select,
                R.id.page_camera_fragment,
                R.id.page_insert_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this) {
            moveTaskToBack(true)
        }
    }


}