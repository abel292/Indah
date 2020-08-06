package com.android_abel.indah._model.local.producto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "_PRODUCTOS")
class ProductoEntity() : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "nombre")
    var nombre: String? = null

    @ColumnInfo(name = "codigo")
    var codigo: String? = null

    @ColumnInfo(name = "urlImagen")
    var urlImagen: String? = null

    @ColumnInfo(name = "unidad")
    var unidad: String? = null

    @ColumnInfo(name = "cantidad")
    var cantidad: Int = 0

    @ColumnInfo(name = "cantidadReserva")
    var cantidadReserva: Int = 0

    @ColumnInfo(name = "precioCompra")
    var precioCompra: Int = 0

    @ColumnInfo(name = "precioVenta")
    var precioVenta: Int = 0

    @ColumnInfo(name = "descripcion")
    var descripcion: String? = null

}