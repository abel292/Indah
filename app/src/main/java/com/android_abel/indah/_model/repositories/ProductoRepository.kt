package com.android_abel.indah._model.repositories

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android_abel.indah._model.local.DataBaseIndah
import com.android_abel.indah._model.local.producto.ProductoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepository(context: Context) {

    var database = DataBaseIndah.getDatabase(context)
    var productoDao = database.productoDao()

    val productosLive: LiveData<List<ProductoEntity>> = productoDao.getProductosLive()


    @WorkerThread
    suspend fun insertProducto(productoEntity: ProductoEntity) = withContext(Dispatchers.IO) {
        productoDao.insert(productoEntity)
    }
}