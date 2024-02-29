package com.anas.aiassistant.shared

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class RunningApp: Application() {
//    override fun onCreate() {
//        super.onCreate()
//        val channel =NotificationChannel(
//            "channel",
//            "running Notification",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//
//
//    }
}