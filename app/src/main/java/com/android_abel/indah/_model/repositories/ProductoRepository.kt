package com.android_abel.indah._model.repositories

import android.content.Context
import android.nfc.Tag
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android_abel.indah._model.local.DataBaseIndah
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.ProductoVendido
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

    @WorkerThread
    suspend fun updateCantidadProducto(list: List<ProductoVendido>) = withContext(Dispatchers.IO) {

        list.forEach { productoVendido ->
            productoVendido.idProducto?.let { id ->
                val producto = productoDao.getSingleProducto(id)
                val restante = producto?.cantidad?.minus(productoVendido.cantidad)
                if (restante != null) {
                    productoDao.updateCantidad(restante, id)
                } else {
                    Log.e("ERROR", "updateCantidadProducto RESTANTE NULL")
                }


            }
        }

    }
}