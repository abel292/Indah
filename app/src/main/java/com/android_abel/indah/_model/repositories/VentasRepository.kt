package com.android_abel.indah._model.repositories

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android_abel.indah._model.local.DataBaseIndah
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VentasRepository(context: Context) {

    var database = DataBaseIndah.getDatabase(context)
    var dao = database.ventaDao()

    val allVentasEntity: LiveData<List<VentaEntity>> = dao.getAllLive()


    @WorkerThread
    suspend fun insertLocal(ventaEntity: VentaEntity) = withContext(Dispatchers.IO) {
        dao.insert(ventaEntity)
    }

    @WorkerThread
    suspend fun update(ventaEntity: VentaEntity) = withContext(Dispatchers.IO) {
        dao.insert(ventaEntity)
    }
}