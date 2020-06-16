package com.android_abel.indah._model.local.producto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductoDao {

    @Query("SELECT * from _PRODUCTOS")
    fun getProductos(): List<ProductoEntity>

    @Query("SELECT * from _PRODUCTOS")
    fun getProductosLive(): LiveData<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(producto: ProductoEntity)
}