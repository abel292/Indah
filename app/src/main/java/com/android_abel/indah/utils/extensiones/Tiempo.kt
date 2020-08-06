package com.android_abel.indah.utils.extensiones

import android.os.Handler

fun postDelayed(delayMillis: Long, task: () -> Unit) {
    Handler().postDelayed(task, delayMillis)
}


fun functionWithParams(delayMillis: Long, task: (String) -> Unit) {

}