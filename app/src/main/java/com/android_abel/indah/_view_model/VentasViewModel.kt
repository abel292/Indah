package com.android_abel.indah._view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android_abel.indah.IndahApplication
import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._model.repositories.CarritoRepository
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
    val carritoRepository = CarritoRepository(application)

    //data lives
    val productosLive = MutableLiveData<List<ProductoEntity>>()
    val clientesLive: LiveData<List<ClienteEntity>> = clientesRepository.clientesLive
    val ventasLive: LiveData<List<VentaEntity>> = repositoryVentas.allVentasEntity
    val carrito = MutableLiveData<ArrayList<ProductoVendidoEntity>>()
    val previewHistorialVentaLive: LiveData<List<PreviewHistorialVenta>> = MutableLiveData()


    fun insertVenta(venta: VentaEntity) {
        GlobalScope.launch {
            repositoryVentas.insertLocal(venta)
            actualizarInventario(venta)
        }
    }

    fun getAllProductos() {
        GlobalScope.launch {
            val productos = repositoryProducto.getProductos()
            if (productos.isNullOrEmpty()) {
                //error.postValue(IndahApplication.applicationContext().getString(R.string.error_cargar_productos))

            } else {
                productosLive.postValue(productos)
            }
        }
    }

    suspend fun actualizarInventario(venta: VentaEntity) {
        if (venta.productosVendidoEntities != null)
            repositoryProducto.updateCantidadProducto(venta.productosVendidoEntities!!)

    }

    fun parseVentasToPreviewHistory(list: List<VentaEntity>) {
        val listPreview = ArrayList<PreviewHistorialVenta>()

    }

    fun getCliente(venta: VentaEntity) {
        GlobalScope.launch {
            var clienteEntity = clientesRepository.getClienteWithID(venta.idCliente ?: -1)
        }
    }

    //TODO CARRITO

    fun getCarrito() {
        GlobalScope.launch {
            val productos = carritoRepository.getProductos()
            if (productos.isNullOrEmpty()) {
                carrito.postValue(ArrayList())
            } else {
                //convertimos los productos en el carritos a entities
                val productosEnCarrito = ArrayList<ProductoVendidoEntity>()

                productos.forEach {
                    //val productoEntity = repositoryProducto.getProductoByID(it.idProducto)
                    productosEnCarrito.add(it)

                }
                carrito.postValue(productosEnCarrito)
            }
        }
    }

    fun updateCarrito(productos: ArrayList<ProductoVendidoEntity>) {
        GlobalScope.launch {
            productos.forEach {
                carritoRepository.updatetLocal(it)
            }
        }
    }

    fun deleteItemCarrito(producto: ProductoVendidoEntity) {
        GlobalScope.launch {
                carritoRepository.deleteItemCarrito(producto.idProducto)
        }
    }

    fun insertCarrito(producto: ProductoVendidoEntity) {
        GlobalScope.launch {
                carritoRepository.insert(producto)
        }
    }


}