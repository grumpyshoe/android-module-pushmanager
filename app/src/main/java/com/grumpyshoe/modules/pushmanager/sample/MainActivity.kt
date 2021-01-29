package com.grumpyshoe.modules.pushmanager.sample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.grumpyshoe.module.pushmanager.PushManager
import com.grumpyshoe.module.pushmanager.impl.PushManagerImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val _pushmanager: PushManager = PushManagerImpl      // get instance of push manager
    private var _token: String? = null                           // save token here for unregister
    private var _registered = false
    private var _subscribed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRegisterState()
        setSubscribeState()

        // register from push notification
        btn_register.setOnClickListener {

            GlobalScope.launch {

                // register for push notification
                _pushmanager.register(
                    context = this@MainActivity,
                    onTokenReceived = {
                        _token = it
                        val msg = "onTokenReceived - token received: $it"
                        Log.d("PushManager", msg)
                        result.onUI { it.text = msg }
                        _registered = true
                        setRegisterState()
                    },
                    onFailure = {
                        val msg = "onFailure - error during registration: ${it?.message}"
                        Log.d("PushManager", msg)
                        result.onUI { it.text = msg }
                    })

            }
        }

        // unregister from push notification
        btn_unregister.setOnClickListener {

            GlobalScope.launch {
                _pushmanager.unregister(this@MainActivity)

                GlobalScope.launch(Dispatchers.Main) {
                    toast("onSuccess -  firebase app instance deleted")
                    result.onUI { it.text = "- not registered -" }

                    _registered = false
                    setRegisterState()
                }
            }
        }


        // handle subscribe
        btn_subscribe.setOnClickListener {
            _pushmanager.subscribeToTopic(
                topic = "wurst",
                onSuccess = {
                    toast("onSuccess -  successfully subscribed")
                    Log.d("PushManager", "onSuccess - successfully subscribed")
                    result.onUI { it.text = "onSuccess  - successfully subscribed" }
                    _subscribed = true
                    setSubscribeState()
                },
                onFailure = { exception ->
                    toast("onFailure -  error while subscribed: ${exception?.message}")
                    Log.d("PushManager", "onFailure - error while subscribed: ${exception?.message}")
                    result.onUI { it.text = "onFailure  - error while subscribed: ${exception?.message}" }
                })
        }


        // handle unsubscribe
        btn_unsubscribe.setOnClickListener {
            _pushmanager.unsubscribeFromTopic(
                topic = "wurst",
                onSuccess = {
                    toast("onSuccess -  successfully unsubscribed")
                    Log.d("PushManager", "onSuccess - successfully unsubscribed")
                    result.text = "onSuccess  - successfully unsubscribed"
                    _subscribed = false
                    setSubscribeState()
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
            val channelDefault = NotificationChannel(
                "default_channel",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channelDefault)

            val channelGlobalMessages = NotificationChannel(
                "channel_global_notifications",
                "Global Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channelGlobalMessages)

            // example: create channel for reminder notifications
            val channelReminder = NotificationChannel(
                "channel_reminder_notifications",
                "Reminder Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channelReminder)
        }
    }

    private fun setRegisterState() {
        btn_register.isEnabled = !_registered
        btn_unregister.isEnabled = _registered
    }

    private fun setSubscribeState() {
        btn_subscribe.isEnabled = !_subscribed
        btn_unsubscribe.isEnabled = _subscribed
    }
}
