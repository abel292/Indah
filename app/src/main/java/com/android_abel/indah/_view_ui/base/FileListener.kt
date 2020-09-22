package com.android_abel.indah._view_ui.base

import android.graphics.Bitmap
import android.net.Uri

interface FileListener {

    fun imageUrlSelectedFromGallery(url: String)
    fun imageUriSelectedFromGallery(bitmap: Bitmap)
}