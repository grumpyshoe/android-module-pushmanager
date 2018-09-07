package com.grumpyshoe.modules.pushmanager.sample

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.grumpyshoe.module.pushmanager.impl.PushmanagerMessagingService
import com.grumpyshoe.module.pushmanager.models.NotificationData
import com.grumpyshoe.module.pushmanager.models.RemoteMessageData

class MyService : PushmanagerMessagingService() {

    override fun handleNotificationPayload(context:Context, remoteMessageData: RemoteMessageData): NotificationData? {
        Log.d("PushManager", "handlePayload - ${remoteMessageData.title} - ${remoteMessageData.body}")

        // create pending intent
        val notificationIntent = Intent(context, NotificationActivity::class.java)
        notificationIntent.putExtra("type", remoteMessageData.title ?: "Default Title"+" "
        +remoteMessageData.body ?: "Default Message")
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // create NotificationData containing all necessary information
        return NotificationData(
                context = context,
                title = remoteMessageData.title ?: "Default Title",
                message = remoteMessageData.body ?: "Default Message",
                channelId = "channel_global_notifications",     // needed for SDK >= Android "O" (Oreo)
                autoCancel = true,
                pendingIntent = contentIntent)
    }
}