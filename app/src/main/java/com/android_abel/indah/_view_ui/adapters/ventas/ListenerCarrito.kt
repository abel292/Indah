package com.android_abel.indah._view_ui.adapters.ventas

import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import java.text.ParsePosition

interface ListenerCarrito {

    fun removeItem(productoEntity: ProductoVendidoEntity, position: Int)
}