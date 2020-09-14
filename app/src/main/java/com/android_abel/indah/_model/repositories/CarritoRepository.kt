package com.android_abel.indah._model.repositories

import android.content.Context
import androidx.annotation.WorkerThread
import com.android_abel.indah._model.local.DataBaseIndah
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarritoRepository (context: Context) {

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

}
