package com.grumpyshoe.module.pushmanager

import android.content.Context
import com.grumpyshoe.module.pushmanager.models.NotificationData
import com.grumpyshoe.module.pushmanager.models.RemoteMessageData


/**
 * <p>PushManager - interface for easy access to FCM</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
interface PushManager {


    /**
     * register device for cloudmessaging
     *
     */
    fun register(context: Context, onTokenReceived: (String) -> Unit, onFailure: (Exception?) -> Unit, handlePayload: (RemoteMessageData) -> NotificationData?)


    /**
     * unregister device from cloudmessaging
     *
     */
    fun unregister(context: Context, token: String)


    /**
     * subscribe to topic
     *
     */
    fun subscribeToTopic(topic: String, onSuccess: (() -> Unit)? = null, onFailure: ((Exception?) -> Unit)? = null)


    /**
     * unsubscribe from topic
     *
     */
    fun unsubscribeFromTopic(topic: String, onSuccess: (() -> Unit)? = null, onFailure: ((Exception?) -> Unit)? = null)


    /**
     * set target class for pending intent
     *
     */
    fun setTargetClass(targetClass: Class<*>)

}