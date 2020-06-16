package com.android_abel.indah._view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.repositories.ProductoRepository

class VentasViewModel(application: Application) : BaseViewModel(application) {

    var repositoryProducto = ProductoRepository(application)

    //data lives
    val productosLive: LiveData<List<ProductoEntity>> = repositoryProducto.productosLive
}