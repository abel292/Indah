package com.android_abel.indah._model.local.productoCarrito

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoProductoVendido {
    @Query("SELECT * from _CARRITO")
    fun getAll(): List<ProductoVendidoEntity>

    @Query("SELECT * from _CARRITO")
    fun getAllLive(): LiveData<List<ProductoVendidoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ventaEntity: ProductoVendidoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(ventaEntity: ProductoVendidoEntity)

    @Delete
    fun remove(ventaEntity: ProductoVendidoEntity)

    @Query("DELETE FROM _carrito")
    fun removeAll()

    @Query("DELETE FROM _CARRITO WHERE idProducto = :id")
    fun deleteById(id: Int)

}