package com.android_abel.indah._model.local.productoCarrito

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoProductoVendido {
    @Query("SELECT * from _CARRITO")
    fun getAll(): List<ProductoVendidoEntity>

    @Query("SELECT * from _CARRITO")
    fun getAllLive(): LiveData<List<ProductoVendidoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ventaEntity: ProductoVendidoEntity)
}