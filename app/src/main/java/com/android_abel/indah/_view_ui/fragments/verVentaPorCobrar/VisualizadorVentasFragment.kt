package com.android_abel.indah._view_ui.fragments.verVentaPorCobrar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_model.VentasViewModel
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah.utils.CustomsConstantes

class VisualizadorVentasFragment : BaseFragment() {

    private lateinit var ventaEntity: VentaEntity
    //viewModels
    val ventasViewModel by lazy {
        ViewModelProviders.of(this).get(VentasViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_visualizador_ventas, container, false)
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
        ventaEntity = argumentProducto?.getSerializable(CustomsConstantes.EXTRAS_VIEW_VENTA) as VentaEntity
        try {
            initObservables()
        } catch (e: Exception) {
            showToast("No se pudo cargar el producto")
        }
    }

    private fun cargarDatosAVista() {

    }
}
