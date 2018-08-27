package com.grumpyshoe.module.pushmanager.models

import android.net.Uri

/**
 * <p>RemoteMessageData is a model for holding all notification information that's available from incomming message</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
data class RemoteMessageData(
        var tag: String? = null,
        var bodyLocalizationKey: String? = null,
        var bodyLocalizationArgs: Array<String>? = null,
        var title: String? = null,
        var body: String? = null,
        var clickAction: String? = null,
        var color: String? = null,
        var icon: String? = null,
        var link: Uri? = null,
        var sound: String? = null,
        var titleLocalizationKey: String? = null,
        var titleLocalizationArgs: Array<String>? = null,
        var data: MutableMap<String, String>? = null,
        var messageType: String? = null,
        var collapseKey: String? = null,
        var from: String? = null,
        var messageId: String? = null,
        var originalPriority: Int? = null,
        var priority: Int? = null,
        var sentTime: Long? = null,
        var to: String? = null,
        var ttl: String? = null)