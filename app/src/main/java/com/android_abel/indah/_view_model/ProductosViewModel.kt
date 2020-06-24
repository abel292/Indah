package com.android_abel.indah._view_model

import android.app.Application
import androidx.lifecycle.LiveData
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.repositories.ProductoRepository

class ProductosViewModel(application: Application) : BaseViewModel(application) {

    var repositoryProducto = ProductoRepository(application)
    val productosLive: LiveData<List<ProductoEntity>> = repositoryProducto.productosLive



}