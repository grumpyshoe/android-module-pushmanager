package com.grumpyshoe.module.pushmanager.models

/**
 * <p>NotificationLight contains all information for handling smartphone-led on incoming message</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
data class NotificationLight(val argb: Int, val onMS: Int, val offMS: Int)