package com.android_abel.indah._view_ui.adapters.ventas

interface OnSecondListenerItemRecyclerView<T> {
    fun onClickItemSecondListener(objects: T, position: Int)
    fun removeItem(objects: T, position: Int)
}