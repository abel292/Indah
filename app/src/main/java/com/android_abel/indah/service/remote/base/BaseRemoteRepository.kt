package com.android_abel.indah.service.remote.base

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseRemoteRepository {

    companion object {
        private const val TAG = "BaseRemoteRepository"
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"
    }

    /**
     * Function that executes the given function on Dispatchers.IO context and switch to Dispatchers.Main context when an error occurs
     * @param callFunction is the function that is returning the wanted object. It must be a suspend function. Eg:
     * override suspend fun loginUser(body: LoginUserBody, emitter: RemoteErrorEmitter): LoginUserResponse?  = safeApiCall( { authApi.loginUser(body)} , emitter)
     * @param emitterResponse is the interface that handles the error messages. The error messages must be displayed on the MainThread, or else they would throw an Exception.
     */
    suspend inline fun <T> safeApiCall(emitterResponse: RemoteErrorEmitter, crossinline callFunction: suspend () -> T): T? {
        return try{
            val myObject = withContext(Dispatchers.IO){ callFunction.invoke() }
            myObject
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                e.printStackTrace()
                Log.e("BaseRemoteRepo", "Call error: ${e.localizedMessage}", e.cause)
                when(e){
                    is HttpException -> {
                        if(e.code() == 401) emitterResponse.onError(ErrorType.SESSION_EXPIRED)
                        else {
                            val body = e.response()?.errorBody()
                            emitterResponse.onError(getErrorMessage(body))
                        }
                    }
                    is SocketTimeoutException -> emitterResponse.onError(ErrorType.TIMEOUT)
                    is IOException -> emitterResponse.onError(ErrorType.NETWORK)
                    else -> emitterResponse.onError(ErrorType.UNKNOWN)
                }
            }
            null
        }
    }

    /**
     * Function that executes the given function in whichever thread is given. Be aware, this is not friendly with Dispatchers.IO,
     * since [RemoteErrorEmitter] is intended to display messages to the user about error from the server/DB.
     * @param callFunction is the function that is returning the wanted object. Eg:
     * override suspend fun loginUser(body: LoginUserBody, emitter: RemoteErrorEmitter): LoginUserResponse?  = safeApiCall( { authApi.loginUser(body)} , emitter)
     * @param emitterResponse is the interface that handles the error messages. The error messages must be displayed on the MainThread, or else they would throw an Exception.
     */
    inline fun <T> safeApiCallNoContext(emitterResponse: RemoteErrorEmitter, callFunction: () -> T): T? {
        return try{
            val myObject = callFunction.invoke()
            myObject
        }catch (e: Exception){
            e.printStackTrace()
            Log.e("BaseRemoteRepo", "Call error: ${e.localizedMessage}", e.cause)
            when(e){
                is HttpException -> {
                    if(e.code() == 401) emitterResponse.onError(ErrorType.SESSION_EXPIRED)
                    else {
                        val body = e.response()?.errorBody()
                        emitterResponse.onError(getErrorMessage(body))
                    }
                }
                is SocketTimeoutException -> emitterResponse.onError(ErrorType.TIMEOUT)
                is IOException -> emitterResponse.onError(ErrorType.NETWORK)
                else -> emitterResponse.onError(ErrorType.UNKNOWN)
            }
            null
        }
    }

    fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(responseBody!!.string())
            when {
                jsonObject.has(MESSAGE_KEY) -> jsonObject.getString(MESSAGE_KEY)
                jsonObject.has(ERROR_KEY) -> jsonObject.getString(ERROR_KEY)
                else -> "Something wrong happened"
            }
        } catch (e: Exception) {
            "Something wrong happened"
        }
    }


}