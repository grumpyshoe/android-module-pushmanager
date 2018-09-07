package com.grumpyshoe.module.pushmanager.impl

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.grumpyshoe.module.pushmanager.PushManager
import com.grumpyshoe.module.pushmanager.models.NotificationData
import com.grumpyshoe.module.pushmanager.models.RemoteMessageData
import org.jetbrains.anko.doAsync


/**
 * <p>PushManagerImpl is based on PushManager and contains all connection logic for handling FCM</p>
 *
 * @version  1.2.0
 * @since    1.0.0
 * @author   grumpyshoe
 *
 */
object PushManagerImpl : PushManager {

    private var onTokenReceived: ((String) -> Unit)? = null


    /**
     * init push
     *
     */
    private fun initPushmanager(context: Context) {
        FirebaseApp.initializeApp(context)
    }


    /**
     * register
     *
     */
    override fun register(context: Context, onTokenReceived: (String) -> Unit, onFailure: (Exception?) -> Unit) {

        this.onTokenReceived = onTokenReceived

        // init firebase
        initPushmanager(context)

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        onFailure(task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result.token

                    // Log and toast
                    this.onTokenReceived?.let { it(token) }
                })
    }


    /**
     * unregister
     *
     */
    override fun unregister(context: Context, token: String) {

        // init firebase
        initPushmanager(context)

        doAsync {
            FirebaseInstanceId.getInstance().deleteInstanceId()
        }
    }


    /**
     * subscribe to topic
     *
     */
    override fun subscribeToTopic(topic: String, onSuccess: (() -> Unit)?, onFailure: ((Exception?) -> Unit)?) {

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        onFailure?.invoke(task.exception)
                        return@OnCompleteListener

                    }

                    onSuccess?.invoke()

                })
    }


    /**
     * unsubscribe from topic
     *
     */
    override fun unsubscribeFromTopic(topic: String, onSuccess: (() -> Unit)?, onFailure: ((Exception?) -> Unit)?) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        onFailure?.invoke(task.exception)
                        return@OnCompleteListener
                    }

                    onSuccess?.invoke()

                })
    }


    /**
     * send registration to server
     *
     */
    fun sendRegistrationToServer(token: String) {
        this.onTokenReceived?.let { it(token) }
    }

}