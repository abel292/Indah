package com.android_abel.indah._view_ui.fragments.ventas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.ProductoVendido
import com.android_abel.indah._view_model.VentasViewModel
import com.android_abel.indah._view_ui.adapters.productos.AdapterProductos
import com.android_abel.indah._view_ui.adapters.ventas.AdapterVentas
import com.android_abel.indah._view_ui.adapters.ventas.ConfigVentaListener
import com.android_abel.indah._view_ui.adapters.ventas.ListenerCarrito
import com.android_abel.indah._view_ui.adapters.ventas.OnClickItemProductoSearched
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import kotlinx.android.synthetic.main.fragment_ventas.*


class VentasFragment : BaseFragment(), BasicMethods,
    OnClickItemProductoSearched,
    ListenerCarrito, ConfigVentaListener {

    //views
    lateinit var fragmentView: View

    //adapters
    lateinit var mAdapter: AdapterVentas
    lateinit var mAdapterBuscarProducto: AdapterProductos

    //global var
    lateinit var productos: List<ProductoEntity>
    var carrito: ArrayList<ProductoEntity> = ArrayList()


    val ventasViewModel by lazy {
        ViewModelProviders.of(this).get(VentasViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_ventas, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
        ventasViewModel.productosLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewSearchProduct(it)
        })
    }

    override fun init() {
        notifyRecyclerViewCarritoItemsVentas(carrito)
        recyclerViewProductosCarrito_f_ventas.layoutManager = LinearLayoutManager(getActivity())
        recyclerViewSearchProduct_ventas.layoutManager = LinearLayoutManager(getActivity())
    }

    override fun initListeners() {

        autoCompleteTextViewVentas.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString())
                if (editable.trim().isNotEmpty()) {
                    showListProductSearch()
                    imageViewClearEdittext.visibility = View.VISIBLE
                } else {
                    hideListProductSearch()
                    imageViewClearEdittext.visibility = View.GONE
                }

            }
        })

        edittextPagoInicial_venta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                var restante= textViewTotalVenta.text.toString().toInt()
                restante = if (editable.trim().isNotEmpty()) {
                    textViewTotalVenta.text.toString().toInt() - edittextPagoInicial_venta.text.toString().toInt()
                } else {
                    textViewTotalVenta.text.toString().toInt() - 0
                }
                showCantidadRestantePagoInicial(restante)
            }
        })

        imageViewClearEdittext.setOnClickListener {
            clearBuscador()
        }

        buttonTerminarVenta.setOnClickListener {
            val listCarrito = mAdapter.listaVendido
        }
    }

    private fun notifyRecyclerViewCarritoItemsVentas(list: ArrayList<ProductoEntity>) {
        mAdapter = AdapterVentas(list)
        mAdapter.listenerCarrito = this
        mAdapter.listenerConfigVenta = this
        recyclerViewProductosCarrito_f_ventas.adapter = mAdapter

    }

    override fun compilandoProductosCarrito(listCarrito: ArrayList<ProductoVendido>) {
        var precioTotal = 0
        listCarrito.forEach { producto ->
            precioTotal += producto.subTotal
        }
        textViewTotalVenta.text = precioTotal.toString()
    }

    private fun notifyRecyclerViewSearchProduct(list: List<ProductoEntity>) {
        productos = list
        mAdapterBuscarProducto = AdapterProductos(list)
        mAdapterBuscarProducto.listener = this
        recyclerViewSearchProduct_ventas.adapter = mAdapterBuscarProducto
    }

    override fun onClickItemProductoSearched(productoEntity: ProductoEntity) {
        addProductoCarrito(productoEntity)
        clearBuscador()
    }

    override fun removeItem(productoEntity: ProductoEntity, position: Int) {
        carrito.remove(productoEntity)
        mAdapter.deleteProducto(productoEntity, position)
    }

    internal fun filter(text: String) {
        val filterdNames: ArrayList<ProductoEntity> = ArrayList()
        if (!productos.isNullOrEmpty())
            for (producto in productos) {
                val nombre = producto.nombre
                if (nombre != null) {
                    if (nombre.toLowerCase().contains(text.toLowerCase())) {
                        if (filterdNames.size < 2)
                            filterdNames.add(producto)
                    }
                }
            }
        mAdapterBuscarProducto.filterList(filterdNames)
    }

    internal fun hideListProductSearch() {
        recyclerViewSearchProduct_ventas.visibility = View.GONE
    }

    internal fun showListProductSearch() {
        recyclerViewSearchProduct_ventas.visibility = View.VISIBLE
    }

    private fun addProductoCarrito(productoEntity: ProductoEntity) {
        mAdapter.addProducto(productoEntity)
    }

    private fun clearBuscador() {
        autoCompleteTextViewVentas.setText("")
        autoCompleteTextViewVentas.clearFocus()
    }

    internal fun showCantidadRestantePagoInicial(cant: Int) {
        textViewDeudaRestante.text = cant.toString()
    }
}
