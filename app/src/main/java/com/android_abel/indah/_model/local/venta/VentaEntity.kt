package com.android_abel.indah._model.local.venta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android_abel.indah._model.local.convertes.ConverterDate
import com.android_abel.indah._model.local.convertes.ConverterProductoVendido
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import java.io.Serializable
import java.util.*


@Entity(tableName = "_VENTAS")
class VentaEntity(): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "productosVendidos")
    @TypeConverters(ConverterProductoVendido::class)
    var productosVendidoEntities: List<ProductoVendidoEntity>? = null

    @ColumnInfo(name = "fecha")
    @TypeConverters(ConverterDate::class)
    var fecha: Date? = null

    @ColumnInfo(name = "pagado")
    var pagado: Boolean? = null

    @ColumnInfo(name = "total")
    var total: Int? = null

    @ColumnInfo(name = "pagoInicial")
    var pagoInicial: Int? = null

    @ColumnInfo(name = "idCliente")
    var idCliente: Int? = null

    @ColumnInfo(name = "formaDePago")
    var formaDePago: String? = null

    @ColumnInfo(name = "descripcion")
    var descripcion: String? = null

}