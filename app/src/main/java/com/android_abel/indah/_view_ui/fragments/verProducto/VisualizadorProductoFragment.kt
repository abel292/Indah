package com.android_abel.indah._view_ui.fragments.verProducto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import com.android_abel.indah.utils.CustomsConstantes
import com.android_abel.indah.utils.extensiones.functionWithParams
import com.android_abel.indah.utils.extensiones.postDelayed
import kotlinx.android.synthetic.main.fragment_visualizador_producto.*

class VisualizadorProductoFragment : BaseFragment() {

    private lateinit var productoEntity: ProductoEntity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_visualizador_producto, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArgument()
        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
    }

    override fun init() {
    }

    override fun initListeners() {
    }

    private fun initArgument() {

        val argumentProducto = arguments
        productoEntity = argumentProducto?.getSerializable(CustomsConstantes.EXTRAS_VIEW_PRODUCT) as ProductoEntity
        try {
            cargarDatosAVista()
            /* postDelayed(10000){
                 showToast("TE PASAS CLAUDIA")
             }*/

            functionWithParams(1000) {
                showToast(it)
            }
        } catch (e: Exception) {
            showToast("No se pudo cargar el producto")
        }
    }

    private fun cargarDatosAVista() {
        textViewNombre.text = productoEntity.nombre
        textViewCodigo.text = productoEntity.codigo
        textViewCantidad.text = productoEntity.cantidad.toString()
        textViewReserva.text = productoEntity.cantidadReserva.toString()
        textViewCompra.text = productoEntity.precioCompra.toString()
        textViewVenta.text = productoEntity.precioVenta.toString()
        textViewDescripcion.text = productoEntity.descripcion
    }
}
