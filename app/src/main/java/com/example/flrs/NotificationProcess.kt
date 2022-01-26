package com.example.flrs

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.LocalDate

class NotificationProcess : BroadcastReceiver() {

    var db_list = mutableListOf<RowModel>()
    lateinit var mFoodsDao: FoodsDao
    var cnt = 0

    override fun onReceive(context: Context, intent: Intent) {


        Toast.makeText(context, "Alarm Ringing...", Toast.LENGTH_SHORT).show()
        // 作りたい処理を書く

        mFoodsDao = FoodsDatabase.getInstance(context).foodsDao()
        db_list = mFoodsDao.getAll().toMutableList()

        for (i in db_list) {
            val register_date = LocalDate.parse(i.register_date)
            val period = register_date.plusDays(i.period.toLong())

            if (period < LocalDate.now()) {
                cnt++
            }
        }

        if (cnt > 0) {
            val contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )
            val notification = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_flrs_foreground)
                .setContentTitle("星先生からのお知らせ")
                .setContentText("${cnt}個の期限切れの食品があります")
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build()
            val manager = NotificationManagerCompat.from(context)
            manager.notify(1, notification)
        }
    }
}