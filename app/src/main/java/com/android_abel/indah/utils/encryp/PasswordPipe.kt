package com.android_abel.indah.utils.encryp

import android.util.Base64

fun decrypt(stringToDecrypt: String): String? {
    var result = ""
    try {
        val base64 = ""
        val data: ByteArray = Base64.decode(stringToDecrypt, Base64.DEFAULT)
        result = String(data, charset("UTF-8"))
    } catch (e: Exception) {
        println("Api:decrypt - Exception: " + e)
    }
    return result
}

fun encrypt(stringToEncrypt: String): String? {
    var result = ""
    try {
        val base64 = ""
        val data = stringToEncrypt.toByteArray(charset("UTF-8"))
        result = Base64.encodeToString(data, Base64.DEFAULT)
    } catch (e: Exception) {
        println("Api:Encrypt - Exception: " + e)
    }
    return result
}