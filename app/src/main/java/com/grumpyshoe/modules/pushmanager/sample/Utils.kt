package com.grumpyshoe.modules.pushmanager.sample

import android.content.Context
import android.widget.TextView
import android.widget.Toast

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()
fun TextView.onUI(action: (TextView) -> Unit) {
    this.post { action(this) }
}