package com.android_abel.indah.service.remote.base

interface OnResponse<T> {
    enum class ResponseType {
        OK, BAD
    }

    fun onResponse(
        responseType: ResponseType,
        entity: T?,
        listEntity: List<T>?
    )

    fun onError(code: Int, error: String?)
}