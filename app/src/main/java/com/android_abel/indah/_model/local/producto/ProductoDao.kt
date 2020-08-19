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

    @Query("SELECT * from _PRODUCTOS WHERE id= :id order by id asc")
    fun getSingleProducto(id: Int): ProductoEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(producto: ProductoEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(producto: ProductoEntity)

    @Query("UPDATE _PRODUCTOS SET cantidad=:cant WHERE id = :id")
    fun updateCantidad(cant: Int, id: Int)
}