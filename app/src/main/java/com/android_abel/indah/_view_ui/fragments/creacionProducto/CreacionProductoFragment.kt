package com.android_abel.indah._view_ui.fragments.creacionProducto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_model.CreacionProductoViewModel
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import kotlinx.android.synthetic.main.fragment_creacion_proyecto.*


class CreacionProductoFragment : BaseFragment(), BasicMethods {

    val creacionProductoViewModel by lazy {
        ViewModelProviders.of(this).get(CreacionProductoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_creacion_proyecto, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {

    }

    override fun init() {
    }

    override fun initListeners() {
        floatingActionButtonGuardar.setOnClickListener {
            if (validate()) {
                val produtoNuevo = generateProducto()
                creacionProductoViewModel.insert(produtoNuevo)
                clearForm()
            } else
                Toast.makeText(context, "Error al insertar", Toast.LENGTH_LONG).show()

        }
    }

    private fun validate(): Boolean {
        return (editTextNombreProducto_creacion.text.isNotEmpty()
                && editTextCodigoProducto_creacion.text.isNotEmpty()
                && editTextCantidad_creacion.text.isNotEmpty()
                && editTextCantidadReserva_creacion.text.isNotEmpty()
                && editTextPrecioCompra_creacion.text.isNotEmpty()
                && editTextPrecioVenta_creacion.text.isNotEmpty()
                && editTextDescripcion_creacion.text.isNotEmpty())
    }

    private fun generateProducto(): ProductoEntity {
        val producto = ProductoEntity()
        producto.nombre = editTextNombreProducto_creacion.text.toString()
        producto.codigo = editTextCodigoProducto_creacion.text.toString()
        producto.cantidad = editTextCantidad_creacion.text.toString().toInt()
        producto.cantidadReserva = editTextCantidadReserva_creacion.text.toString().toInt()
        producto.precioCompra = editTextPrecioCompra_creacion.text.toString().toInt()
        producto.precioVenta = editTextPrecioVenta_creacion.text.toString().toInt()
        producto.descripcion = editTextDescripcion_creacion.text.toString()

        return producto
    }

    private fun clearForm() {
        editTextNombreProducto_creacion.setText("")
        editTextCodigoProducto_creacion.setText("")
        editTextCantidad_creacion.setText("")
        editTextCantidadReserva_creacion.setText("")
        editTextPrecioCompra_creacion.setText("")
        editTextPrecioVenta_creacion.setText("")
        editTextDescripcion_creacion.setText("")
    }

}
