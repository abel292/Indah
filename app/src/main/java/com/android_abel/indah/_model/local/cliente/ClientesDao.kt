package com.android_abel.indah._model.local.cliente

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity

@Dao
interface ClientesDao {

    @Query("SELECT * from _CLIENTES")
    fun getAll(): List<ClienteEntity>

    @Query("SELECT * from _CLIENTES")
    fun getAllLive(): LiveData<List<ClienteEntity>>

    @Query("SELECT * from _CLIENTES WHERE id= :id order by id asc")
    fun getSingle(id: Int): ClienteEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cliente: ClienteEntity)

    @Delete
    fun remove(cliente: ClienteEntity)

}