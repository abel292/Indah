package com.android_abel.indah._view_ui.adapters.ventas

import com.android_abel.indah._model.local.producto.ProductoEntity
import java.text.ParsePosition

interface ListenerCarrito {

    fun removeItem(productoEntity: ProductoEntity, position: Int)
}