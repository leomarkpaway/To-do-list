package com.leomarkpaway.todolist.common.util

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.leomarkpaway.todolist.R

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannel(channelID: String, channelName: String) {
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(this, NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
}

fun Context.createNotification(channelID: String, title: String, content: String) {
    val notificationId = 1 // Unique ID for the notification
    val notification = NotificationCompat.Builder(this, channelID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setAutoCancel(true)
        .build()

    val manager = getSystemService(this, NotificationManager::class.java)
    manager?.notify(notificationId, notification)
}

fun Context.createNotification(channelID: String, title: String, content: String, intent: Intent) {
    val notificationId = 1 // Unique ID for the notification
    // Create an intent for when the notification is tapped
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val notification = NotificationCompat.Builder(this, channelID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    val manager = getSystemService(this, NotificationManager::class.java)
    manager?.notify(notificationId, notification)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getManifestNotification() = Manifest.permission.POST_NOTIFICATIONS

@RequiresApi(Build.VERSION_CODES.O)
fun Activity.checkNotificationPermissions(channelID: String): Boolean {
    val notificationManager = NotificationManagerCompat.from(this)
    val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
    val channelEnabled = notificationManager.getNotificationChannel(channelID)?.importance != NotificationManager.IMPORTANCE_NONE
    return areNotificationsEnabled && channelEnabled
}
