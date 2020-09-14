package com.android_abel.indah._model.local.productoCarrito

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "_CARRITO")
class ProductoVendidoEntity : Serializable {

    @PrimaryKey()
    var idProducto: Int = 0
    var cantidad: Int = 1
    var precioVenta: Int = 0
    var subTotal: Int = 0

    constructor(idProducto: Int, cantidad: Int, precioVenta: Int, subTotal: Int) {
        this.idProducto = idProducto
        this.cantidad = cantidad
        this.precioVenta = precioVenta
        this.subTotal = subTotal
    }

    override fun toString(): String {
        return "ProductoVendido(idProducto=$idProducto, cantidad=$cantidad, precioVenta=$precioVenta, subTotal=$subTotal)\n"
    }


}