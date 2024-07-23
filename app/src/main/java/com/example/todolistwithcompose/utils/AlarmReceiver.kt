package com.example.todolistwithcompose.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todolistwithcompose.R

class AlarmReceiver: BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val title = intent?.getStringExtra(ALARM_TITLE)
            val notification = NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(intent?.getStringExtra(ALARM_CONTENT))
                .setSmallIcon(R.drawable.baseline_cruelty_free_24)
                .build()
            createNotificationChannel(notificationManager)
            notificationManager.notify(title , title.hashCode(), notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "alarm_bogdan_chanel"
        private const val ALARM_TITLE = "title"
        private const val ALARM_CONTENT = "content"

        fun newAlarmIntent(context: Context, title:String, content:String): Intent {
            return Intent(context, AlarmReceiver::class.java).apply {
                putExtra(ALARM_TITLE, title)
                putExtra(ALARM_CONTENT, content)
            }
        }
    }
}