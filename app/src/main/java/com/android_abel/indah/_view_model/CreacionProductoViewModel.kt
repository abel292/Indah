package com.android_abel.indah._view_model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.repositories.FirebaseRepository
import com.android_abel.indah._model.repositories.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreacionProductoViewModel(application: Application) : BaseViewModel(application) {

    private val productoRepository = ProductoRepository(application)
    private val firebaseRepository = FirebaseRepository(application)

    fun insert(productoEntity: ProductoEntity) = viewModelScope.launch(Dispatchers.IO) {
        productoRepository.insertProducto(productoEntity)
    }

    fun update(productoEntity: ProductoEntity) = viewModelScope.launch(Dispatchers.IO) {
        productoRepository.updateProducto(productoEntity)
    }

    fun uploadImage(bitmap: Bitmap){
        GlobalScope.launch {
            firebaseRepository.uploadImage("abel",bitmap)
        }
    }
}