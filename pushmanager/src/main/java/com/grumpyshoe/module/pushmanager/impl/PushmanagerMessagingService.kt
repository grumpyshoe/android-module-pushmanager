package com.grumpyshoe.module.pushmanager.impl

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.grumpyshoe.module.pushmanager.models.NotificationData
import com.grumpyshoe.module.pushmanager.models.NotificationType
import com.grumpyshoe.module.pushmanager.models.RemoteMessageData

/**
 * <p>PushmanagerMessagingService is the service implementation fro handling incoming fcm-messages.</p>
 *
 * @version  1.2.0
 * @since    1.0.0
 * @author   grumpyshoe
 *
 */
abstract class PushmanagerMessagingService : FirebaseMessagingService() {

    val pushManager = PushManagerImpl

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "Message reveived")

        val remoteMessageData = RemoteMessageData()

        remoteMessageData.messageType = remoteMessage.messageType
        remoteMessageData.collapseKey = remoteMessage.collapseKey
        remoteMessageData.from = remoteMessage.from
        remoteMessageData.messageId = remoteMessage.messageId
        remoteMessageData.originalPriority = remoteMessage.originalPriority
        remoteMessageData.priority = remoteMessage.priority
        remoteMessageData.sentTime = remoteMessage.sentTime
        remoteMessageData.to = remoteMessage.to

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {

            remoteMessageData.data = remoteMessage.data

        }

        // Check if message contains a notification payload.
        var notification = remoteMessage.notification
        if (notification != null) {

            remoteMessageData.tag = notification.tag
            remoteMessageData.bodyLocalizationKey = notification.bodyLocalizationKey
            remoteMessageData.bodyLocalizationArgs = notification.bodyLocalizationArgs
            remoteMessageData.title = notification.title
            remoteMessageData.body = notification.body
            remoteMessageData.clickAction = notification.clickAction
            remoteMessageData.color = notification.color
            remoteMessageData.icon = notification.icon
            remoteMessageData.link = notification.link
            remoteMessageData.sound = notification.sound
            remoteMessageData.titleLocalizationKey = notification.titleLocalizationKey
            remoteMessageData.titleLocalizationArgs = notification.titleLocalizationArgs
        }

        sendNotification(handleNotificationPayload(applicationContext, remoteMessageData))

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    /**
     * custom implementation of payload handling
     *
     */
    abstract fun handleNotificationPayload(context:Context, remoteMessageData : RemoteMessageData) : NotificationData


    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: " + token!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        token?.let {
            pushManager.sendRegistrationToServer(token)
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(notificationData: NotificationData) {

        // generate notification from notification data
        val notification = generateNotification(notificationData)

        // show notification
        val notificationManager = notificationData.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = when {
            notificationData.notificationId != null -> notificationData.notificationId
            notificationData.notificationType == NotificationType.STACK -> System.currentTimeMillis().toInt()
            else -> 0
        }

        notificationManager.notify(notificationId, notification)
    }


    /**
     * generate notification item
     *
     */
    private fun generateNotification(notificationData: NotificationData, addPublicVersion: Boolean = true): Notification {
        // set required data
        val notificationBuilder = NotificationCompat.Builder(notificationData.context, notificationData.channelId)
                .setSmallIcon(notificationData.smallIcon)
                .setContentTitle(notificationData.title)
                .setContentText(notificationData.message)
                .setAutoCancel(notificationData.autoCancel)
                .setSound(notificationData.soundUri)


        // set optional data
        notificationData.color?.let { notificationBuilder.color = it }
        notificationData.colorize?.let { notificationBuilder.setColorized(it) }
        notificationData.contentInfo?.let { notificationBuilder.setContentInfo(it) }
        notificationData.category?.let { notificationBuilder.setCategory(it) }
        notificationData.light?.let { notificationBuilder.setLights(it.argb, it.onMS, it.offMS) }
        notificationData.timeoutAfter?.let { notificationBuilder.setTimeoutAfter(it) }
        notificationData.subtext?.let { notificationBuilder.setSubText(it) }
        if (addPublicVersion) {
            notificationData.publicVersion?.let {
                notificationBuilder.setPublicVersion(generateNotification(notificationData.publicVersion, false))
            }
        }
        notificationData.onGoing?.let { notificationBuilder.setOngoing(it) }
        notificationData.ticker?.let { notificationBuilder.setTicker(it) }
        notificationData.useChronometer?.let { notificationBuilder.setUsesChronometer(it) }
        notificationData.pendingIntent?.let { notificationBuilder.setContentIntent(it) }

        return notificationBuilder.build()
    }


    companion object {

        private val TAG = "Pushmanager"

    }


}