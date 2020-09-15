package com.android_abel.indah._view_ui.fragments.ventas

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import com.android_abel.indah._view_model.VentasViewModel
import com.android_abel.indah._view_ui.adapters.productos.AdapterProductos
import com.android_abel.indah._view_ui.adapters.ventas.*
import com.android_abel.indah._view_ui.base.BaseFragmentRecycler
import com.android_abel.indah._view_ui.base.BasicMethods
import com.android_abel.indah._view_ui.base.EscanerListener
import kotlinx.android.synthetic.main.fragment_ventas.*
import java.util.*
import kotlin.collections.ArrayList


class VentasFragment : BaseFragmentRecycler(), BasicMethods,
    OnListenerItemRecyclerView<ProductoEntity>,
    ListenerCarrito, ConfigVentaListener,
    DraggableViewListener, EscanerListener {

    companion object {
        @JvmStatic
        fun newInstance() =
            VentasFragment()
    }

    //adapters
    lateinit var mAdapter: AdapterVentas
    lateinit var mAdapterBuscarProducto: AdapterProductos

    //global var
    lateinit var productos: List<ProductoEntity>
    var carrito: ArrayList<ProductoVendidoEntity>? = null//inicia una vez
    lateinit var mContext: Context

    val ventasViewModel by lazy {
        ViewModelProviders.of(this).get(VentasViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context

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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        try {
            ventasViewModel.getAllProductos()
            ventasViewModel.getCarrito()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun initObservables() {
        ventasViewModel.productosLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewSearchProduct(it)
        })

        ventasViewModel.carrito.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewCarritoItemsVentas(it)
        })

    }

    override fun init() {

        recyclerViewProductosCarrito_f_ventas.layoutManager = LinearLayoutManager(mContext)
        recyclerViewSearchProduct_ventas.layoutManager = LinearLayoutManager(mContext)

    }

    override fun initListeners() {

        //TODO clicks

        imageViewClearEdittext.setOnClickListener {
            clearBuscador()
        }

        imageViewConfigVenta.setOnClickListener {
            it.goTo(R.id.action_ventasFragment_to_configVentaFragment)
        }


        //TODO listeners

        scrollPadre_ventas.setOnScrollChangeListener { _: NestedScrollView, scrollX: Int, scrollY: Int, _: Int, _: Int ->

            if (scrollY > 0 || scrollY < 0) {
                imageViewConfigVenta.visibility = View.GONE
            } else {
                imageViewConfigVenta.visibility = View.VISIBLE
            }
        }

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
    }

    private fun notifyRecyclerViewCarritoItemsVentas(list: ArrayList<ProductoVendidoEntity>) {
        carrito = list
        mAdapter = AdapterVentas(list, mContext, recyclerViewProductosCarrito_f_ventas)
        mAdapter.listenerCarrito = this
        mAdapter.listenerConfigVenta = this
        recyclerViewProductosCarrito_f_ventas.adapter = mAdapter
        recyclerViewProductosCarrito_f_ventas.visibility = View.VISIBLE
        if (list.isNotEmpty())
            recyclerViewSearchProduct_ventas.visibility = View.GONE

    }

    override fun compilandoProductosCarrito(listCarrito: ArrayList<ProductoVendidoEntity>) {
        var precioTotal = 0
        listCarrito.forEach { producto ->
            precioTotal += producto.subTotal
        }

        //precio total
        ventasViewModel.updateCarrito(listCarrito)
        precioTotal.toString()

    }

    private fun notifyRecyclerViewSearchProduct(list: List<ProductoEntity>) {
        productos = list
        mAdapterBuscarProducto = AdapterProductos(mContext, list, recyclerViewSearchProduct_ventas)
        mAdapterBuscarProducto.listener = this
        recyclerViewSearchProduct_ventas.adapter = mAdapterBuscarProducto
        recyclerViewSearchProduct_ventas.visibility = View.VISIBLE
    }


    override fun removeItem(productoEntity: ProductoVendidoEntity, position: Int) {
        carrito?.remove(productoEntity)
        mAdapter.deleteProducto(productoEntity, position)
        ventasViewModel.deleteItemCarrito(productoEntity)
    }

    internal fun filter(text: String) {
        val filterdNames: ArrayList<ProductoEntity> = ArrayList()
        if (!productos.isNullOrEmpty())
            for (producto in productos) {
                val nombre = producto.nombre
                if (nombre != null) {
                    if (nombre.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                        if (filterdNames.size < 2)
                            filterdNames.add(producto)
                    }
                }
            }
        mAdapterBuscarProducto.filterList(filterdNames)
    }

    internal fun hideListProductSearch() {
        recyclerViewSearchProduct_ventas.visibility = View.GONE
        recyclerViewProductosCarrito_f_ventas.visibility = View.VISIBLE

    }

    internal fun showListProductSearch() {
        recyclerViewProductosCarrito_f_ventas.visibility = View.GONE
        recyclerViewSearchProduct_ventas.visibility = View.VISIBLE

    }

    private fun addProductoCarrito(productoEntity: ProductoEntity) {
        productoEntity.id?.let { id -> ProductoVendidoEntity(id, productoEntity.nombre ?: "", 1, productoEntity.precioVenta, 0) }?.let {
            mAdapter.addProducto(it)
            ventasViewModel.insertCarrito(it)
        }

    }

    private fun clearBuscador() {
        autoCompleteTextViewVentas.setText("")
        autoCompleteTextViewVentas.clearFocus()
    }

    private fun validateForms(): Boolean {
        return (mAdapter.listaVendido.size > 0)
    }

    override fun onClickItem(productoEntity: ProductoEntity, position: Int) {
        addProductoCarrito(productoEntity)
        clearBuscador()
        hideKeyBoard()
    }

    override fun onMoveDragger(v: View) {

    }

    override fun onDropRitgh() {
        showToast("right")
        openScaner(this)

    }

    override fun onDropLeft() {
        showToast("left")
        searchWithKeyboard()

    }

    override fun positionInitial(v: View) {
    }

    override fun codeFromScanner(code: String) {
        autoCompleteTextViewVentas.setText(code)
    }

    override fun codeNoFound() {
    }

    private fun searchWithKeyboard() {
        autoCompleteTextViewVentas.visibility = View.VISIBLE
        autoCompleteTextViewVentas.requestFocus()
        autoCompleteTextViewVentas.showKeyboard()
    }

}
