package com.android_abel.indah._view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._model.repositories.ClientesRepository
import com.android_abel.indah._model.repositories.ProductoRepository
import com.android_abel.indah._model.repositories.VentasRepository
import com.android_abel.indah._view_ui.adapters.historialVentas.PreviewHistorialVenta
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VentasViewModel(application: Application) : BaseViewModel(application) {

    var repositoryProducto = ProductoRepository(application)
    val repositoryVentas = VentasRepository(application)
    val clientesRepository = ClientesRepository(application)

    //data lives
    val productosLive: LiveData<List<ProductoEntity>> = repositoryProducto.productosLive
    val clientesLive: LiveData<List<ClienteEntity>> = clientesRepository.clientesLive
    val ventasLive: LiveData<List<VentaEntity>> = repositoryVentas.allVentasEntity
    val previewHistorialVentaLive: LiveData<List<PreviewHistorialVenta>> = MutableLiveData()


    fun insertVenta(venta: VentaEntity) {
        GlobalScope.launch {
            repositoryVentas.insertLocal(venta)
            actualizarInventario(venta)
        }
    }

    suspend fun actualizarInventario(venta: VentaEntity) {
        if (venta.productosVendidos != null)
            repositoryProducto.updateCantidadProducto(venta.productosVendidos!!)

    }

    fun parseVentasToPreviewHistory(list: List<VentaEntity>) {
        val listPreview = ArrayList<PreviewHistorialVenta>()

    }

    fun getCliente(venta: VentaEntity) {
        GlobalScope.launch {
            var clienteEntity = clientesRepository.getClienteWithID(venta.idCliente ?: -1)
        }
    }

}