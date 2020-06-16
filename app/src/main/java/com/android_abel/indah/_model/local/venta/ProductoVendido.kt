package com.android_abel.indah._model.local.venta

class ProductoVendido {
    var idProducto: Int? = null
    var cantidad: Int? = null
    var precioVenta: Int? = null
    var subTotal: Int? = null

    constructor(idProducto: Int?, cantidad: Int?, precioVenta: Int?, subTotal: Int?) {
        this.idProducto = idProducto
        this.cantidad = cantidad
        this.precioVenta = precioVenta
        this.subTotal = subTotal
    }

    override fun toString(): String {
        return "ProductoVendido(idProducto=$idProducto, cantidad=$cantidad, precioVenta=$precioVenta, subTotal=$subTotal)\n"
    }


}