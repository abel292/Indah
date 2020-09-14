package com.android_abel.indah._model.repositories

import android.content.Context
import android.content.LocusId
import androidx.annotation.WorkerThread
import androidx.room.Query
import com.android_abel.indah._model.local.DataBaseIndah
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarritoRepository(context: Context) {

    var database = DataBaseIndah.getDatabase(context)
    var dao = database.carritoDao()


    @WorkerThread
    suspend fun getProductos(): List<ProductoVendidoEntity>? =
        withContext(Dispatchers.IO)
        {
            if (dao.getAll().isNullOrEmpty())
                return@withContext null
            else
                return@withContext dao.getAll()

        }

    @WorkerThread
    suspend fun insertLocal(vendidoEntity: ProductoVendidoEntity) = withContext(Dispatchers.IO) {
        dao.insert(vendidoEntity)
    }

    @WorkerThread
    suspend fun clearCarrito() = withContext(Dispatchers.IO) {
        dao.removeAll()
    }
    @WorkerThread
    suspend fun deleteItemCarrito(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }

    @WorkerThread
    suspend fun updatetLocal(vendidoEntity: ProductoVendidoEntity) = withContext(Dispatchers.IO) {
        dao.update(vendidoEntity)
    }



}
