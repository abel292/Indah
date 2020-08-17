package com.android_abel.indah._view_ui.base

interface EscanerListener {

    fun codeFromScanner(code: String)
    fun codeNoFound()
}