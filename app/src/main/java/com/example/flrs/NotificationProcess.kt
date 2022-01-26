package com.example.flrs

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationProcess : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(context, "Alarm Ringing...", Toast.LENGTH_SHORT).show()
        // 作りたい処理を書く
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notification = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.ic_flrs_foreground)
            .setContentTitle("れんちょんからのお知らせ")
            .setContentText("にゃんぱすー")
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()
        val manager = NotificationManagerCompat.from(context)
        manager.notify(1, notification)
    }

}