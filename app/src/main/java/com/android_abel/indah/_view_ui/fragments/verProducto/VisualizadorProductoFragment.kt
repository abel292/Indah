package com.android_abel.indah._view_ui.fragments.verProducto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah.utils.CustomsConstantes
import com.android_abel.indah.utils.extensiones.functionWithParams
import com.bumptech.glide.Glide
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
        contraintPadreVisualizadorProducto.setTransition(R.id.end, R.id.start)
        contraintPadreVisualizadorProducto.progress = 1f
        contraintPadreVisualizadorProducto.setTransitionDuration(700)
        contraintPadreVisualizadorProducto.transitionToStart()
    }

    override fun initListeners() {

        floatingActionButtonEditarProducto.setOnClickListener {
            fragmentView.goToWithProducto(R.id.action_visualizadorProductoFragment_to_creacionProyectoFragment, productoEntity)
        }
    }

    private fun initArgument() {

        val argumentProducto = arguments
        productoEntity = argumentProducto?.getSerializable(CustomsConstantes.EXTRAS_VIEW_PRODUCT) as ProductoEntity
        try {
            cargarDatosAVista()
            functionWithParams(1000) {
                showToast(it)
            }
        } catch (e: Exception) {
            showToast(getString(R.string.error_cargar_vista_con_producto))
        }
    }

    private fun cargarDatosAVista() {
        textViewNombre.text = productoEntity.nombre
        textViewCodigo.text = productoEntity.codigo
        textViewCantidad.text = productoEntity.cantidad.toString()
        textViewReserva.text = productoEntity.cantidadReserva.toString()
        textViewCompra.text = productoEntity.precioCompra.toString()
        textViewVenta.text = "$" + productoEntity.precioVenta.toString() ?: ""
        textViewGanancia.text = (productoEntity.precioVenta - productoEntity.precioCompra).toString()
        textViewDescripcion.text = productoEntity.descripcion

        Glide.with(requireContext())
            .load(productoEntity.urlImagen)
            .placeholder(R.drawable.ic_scan)
            .error(R.drawable.ic_scan)
            .centerCrop()
            .into(imageViewImageProducto)
    }
}
