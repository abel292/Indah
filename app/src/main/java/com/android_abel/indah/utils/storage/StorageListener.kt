package com.android_abel.indah.utils.storage

interface StorageListener {

    fun progress(value: Int)

    fun onSuccess(url: String?)

    fun onError(description: String?)

    fun onReschedule(description: String?)
}