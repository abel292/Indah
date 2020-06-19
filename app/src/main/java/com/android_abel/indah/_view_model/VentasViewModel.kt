package com.android_abel.indah._view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._model.repositories.ProductoRepository
import com.android_abel.indah._model.repositories.VentasRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VentasViewModel(application: Application) : BaseViewModel(application) {

    var repositoryProducto = ProductoRepository(application)
    val repositoryVentas = VentasRepository(application)

    //data lives
    val productosLive: LiveData<List<ProductoEntity>> = repositoryProducto.productosLive//data lives

    val ventasLive: LiveData<List<VentaEntity>> = repositoryVentas.allVentasEntity

    fun insertVenta(venta: VentaEntity) {
        GlobalScope.launch {
            repositoryVentas.insertLocal(venta)
        }
    }
}