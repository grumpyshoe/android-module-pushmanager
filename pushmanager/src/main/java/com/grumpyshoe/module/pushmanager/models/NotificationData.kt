package com.grumpyshoe.module.pushmanager.models

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import com.grumpyshoe.pushmanager.R

/**
 * <p>NotificationData is a wrapper for creating a custom notification based on a incomming remote message.</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
data class NotificationData(val context: Context,
                            val title: String,
                            val message: String,
                            val soundUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                            val smallIcon: Int = R.drawable.ic_baseline_notifications_24px,
                            val autoCancel: Boolean = true,
                            val channelId: String = context.getString(R.string.default_notification_channel_id),
                            var color: Int? = null,
                            val colorize: Boolean = false,
                            val contentInfo: String? = null,
                            val category: String? = null,
                            val light: NotificationLight? = null,
                            val timeoutAfter: Long? = null,
                            val subtext: String? = null,
                            val publicVersion: NotificationData? = null,
                            val onGoing: Boolean? = null,
                            val ticker: String?= null,
                            val useChronometer: Boolean?= null,
                            val notificationId: Int = 0)