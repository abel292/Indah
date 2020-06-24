package com.android_abel.indah._view_ui.adapters.ventas

import com.android_abel.indah._model.local.producto.ProductoEntity
import java.text.FieldPosition
import java.util.*

interface OnListenerItemRecyclerView<T> {
    fun onClickItem(objects: T, position: Int)

}