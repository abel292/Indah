package com.android_abel.indah._view_ui.adapters.ventas

import com.android_abel.indah._model.local.venta.ProductoVendido

interface ConfigVentaListener {
    fun compilandoProductosCarrito(listCarrito: ArrayList<ProductoVendido>)
}