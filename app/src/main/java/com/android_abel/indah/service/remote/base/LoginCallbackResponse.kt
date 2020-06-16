package com.android_abel.indah.service.remote.base

interface LoginCallbackResponse {
    fun onSuccesfull()
    fun onErrorCredenciales()
    fun onErrorConexionVPN()
    fun onError()

}