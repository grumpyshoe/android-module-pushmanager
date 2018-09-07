package com.grumpyshoe.modules.pushmanager.sample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.grumpyshoe.module.pushmanager.PushManager
import com.grumpyshoe.module.pushmanager.models.NotificationData
import com.grumpyshoe.module.pushmanager.impl.PushManagerImpl
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent


class MainActivity : AppCompatActivity() {

    val pushmanager: PushManager = PushManagerImpl      // get instance of push manager
    private var token: String? = null                   // save token here for unregister


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // register for push notification
        pushmanager.register(
                context = this,
                onTokenReceived = {
                    this.token = it
                    val msg = "onTokenReceived - token received: $it"
                    Log.d("PushManager", msg)
                    result.onUI { it.text = msg }
                },
                onFailure = {
                    val msg = "onFailure - error during registration: ${it?.message}"
                    Log.d("PushManager", msg)
                    result.onUI { it.text = msg }
                },
                handlePayload = {

                    val msg = "handlePayload - ${it.title} - ${it.body}"
                    Log.d("PushManager", msg)

                    // update ui content
                    result.onUI { it.text = msg }

                    // create pending intent
                    val notificationIntent = Intent(applicationContext, NotificationActivity::class.java)
                    notificationIntent.putExtra("type", it.title ?: "Default Title" + " " + it.body ?: "Default Message")
                    notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                    // create NotificationData containing all necessary information
                    NotificationData(
                            context = this,
                            title = it.title ?: "Default Title",
                            message = it.body ?: "Default Message",
                            channelId = "channel_global_notifications",     // needed for SDK >= Android "O" (Oreo)
                            autoCancel = true,
                            pendingIntent = contentIntent)
                })


        // unregister from push notification
        btn_unregister.setOnClickListener {
            token?.let {
                pushmanager.unregister(this, it)
            }
        }


        // handle subscribe
        btn_subscribe.setOnClickListener {
            pushmanager.subscribeToTopic(
                    topic = "wurst",
                    onSuccess = {
                        toast("onSuccess -  successfully subscribed")
                        Log.d("PushManager", "onSuccess - successfully subscribed")
                        result.onUI { it.text = "onSuccess  - successfully subscribed" }
                    },
                    onFailure = { exception ->
                        toast("onFailure -  error while subscribed: ${exception?.message}")
                        Log.d("PushManager", "onFailure - error while subscribed: ${exception?.message}")
                        result.onUI { it.text = "onFailure  - error while subscribed: ${exception?.message}" }
                    })
        }


        // handle unsubscribe
        btn_unsubscribe.setOnClickListener {
            pushmanager.unsubscribeFromTopic(
                    topic = "wurst",
                    onSuccess = {
                        toast("onSuccess -  successfully unsubscribed")
                        Log.d("PushManager", "onSuccess - successfully unsubscribed")
                        result.text = "onSuccess  - successfully unsubscribed"
                    },
                    onFailure = {
                        toast("onFailure -  error while unsubscribe: ${it?.message}")
                        Log.d("PushManager", "onFailure - error while unsubscribe: ${it?.message}")
                        result.text = "onFailure  - error while unsubscribe: ${it?.message}"
                    })
        }


        // get instance of NotificationManager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // example: create channel for global notifications
            val channelGlobalMessages = NotificationChannel("channel_global_notifications",
                    "Global Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channelGlobalMessages)

            // example: create channel for reminder notifications
            val channelReminder = NotificationChannel("channel_reminder_notifications",
                    "Reminder Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channelReminder)
        }
    }
}
