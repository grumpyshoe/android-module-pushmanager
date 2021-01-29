package com.grumpyshoe.module.pushmanager.impl

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.grumpyshoe.module.pushmanager.PushManager


/**
 * <p>PushManagerImpl is based on PushManager and contains all connection logic for handling FCM</p>
 *
 * @version  1.3.0
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
    override suspend fun register(context: Context, onTokenReceived: (String) -> Unit, onFailure: (Exception?) -> Unit) {

        this.onTokenReceived = onTokenReceived

        // init firebase
        initPushmanager(context)

        FirebaseInstallations.getInstance().id
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onFailure(task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result

                // Log and toast
                token?.let {
                    this.onTokenReceived?.let { it(token) }
                }
            })
    }


    /**
     * unregister
     *
     */
    override suspend fun unregister(context: Context) {

        // init firebase
        initPushmanager(context)

        FirebaseInstallations.getInstance().delete()
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