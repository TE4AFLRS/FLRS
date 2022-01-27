package com.example.flrs

import android.app.PendingIntent
import android.app.TaskStackBuilder
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
    var pastcnt = 0
    var beforecnt = 0

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(context, "Alarm Ringing...", Toast.LENGTH_SHORT).show()
        // 作りたい処理を書く

        mFoodsDao = FoodsDatabase.getInstance(context).foodsDao()
        db_list = mFoodsDao.getAll().toMutableList()

        for (i in db_list) {
            val register_date = LocalDate.parse(i.register_date)
            val period = register_date.plusDays(i.period.toLong())

            if (period < LocalDate.now()) {
                pastcnt++
            }
            if (period == LocalDate.now().plusDays(1)) {
                beforecnt++
            }
        }

        if (pastcnt > 0) {
            val contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(Intent(context, MainActivity::class.java))
            val notification = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_flrs_foreground)
                .setContentTitle("星先生からのお知らせ")
                .setContentText("${pastcnt}個の期限切れの食品があります")
                .setContentIntent(
                    stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setAutoCancel(true)
                .build()
            val manager = NotificationManagerCompat.from(context)
            manager.notify(1, notification)
        }
        if (beforecnt > 0) {
            val contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(Intent(context, MainActivity::class.java))
            val notification = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_flrs_foreground)
                .setContentTitle("星先生からのお知らせ")
                .setContentText("${beforecnt}個の期限切れが近い食品があります。")
                .setContentIntent(
                    stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setAutoCancel(true)
                .build()
            val manager = NotificationManagerCompat.from(context)
            manager.notify(2, notification)
        }
    }
}