package com.android_abel.indah.service.remote.base

/**
 * Created by eagskunst in 1/12/2019.
 */
interface RemoteErrorEmitter {
    fun onError(msg: String)
    fun onError(errorType: ErrorType)
}