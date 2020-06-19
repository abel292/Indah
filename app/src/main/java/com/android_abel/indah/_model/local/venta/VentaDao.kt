package com.android_abel.indah._model.local.venta

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VentaDao {
    @Query("SELECT * from _VENTAS")
    fun getAll(): List<VentaEntity>

    @Query("SELECT * from _VENTAS")
    fun getAllLive(): LiveData<List<VentaEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ventaEntity: VentaEntity)
}