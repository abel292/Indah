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
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.ProductoVendido
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_model.VentasViewModel
import com.android_abel.indah._view_ui.adapters.clientes.AdapterClientes
import com.android_abel.indah._view_ui.adapters.productos.AdapterProductos
import com.android_abel.indah._view_ui.adapters.ventas.*
import com.android_abel.indah._view_ui.base.BaseFragmentRecycler
import com.android_abel.indah._view_ui.base.BasicMethods
import com.android_abel.indah.utils.CustomsConstantes
import kotlinx.android.synthetic.main.fragment_ventas.*
import java.util.*
import kotlin.collections.ArrayList


class VentasFragment : BaseFragmentRecycler(), BasicMethods,
    OnListenerItemRecyclerView<ProductoEntity>,
    ListenerCarrito, ConfigVentaListener, OnSecondListenerItemRecyclerView<ClienteEntity> {

    companion object {
        @JvmStatic
        fun newInstance() =
            VentasFragment()
    }

    //adapters
    lateinit var mAdapter: AdapterVentas
    lateinit var mAdapterBuscarProducto: AdapterProductos
    lateinit var mAdapterBuscarCliente: AdapterClientes

    //global var
    lateinit var productos: List<ProductoEntity>
    lateinit var clientes: List<ClienteEntity>
    var clienteSeleccionado: ClienteEntity? = null
    var carrito: ArrayList<ProductoEntity> = ArrayList()
    lateinit var mContext: Context


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
        mContext = requireContext()

        initArgument()
        initObservables()
        init()
        initListeners()
    }

    private fun initArgument() {
        val argument = arguments
        val isInit = argument?.getBoolean(CustomsConstantes.EXTRAS_VENTAS_MODO_INICIO, true) ?: true

        if (isInit)
            content_padre_f_ventas.visibility = View.GONE
        else
            content_padre_f_ventas.visibility = View.VISIBLE

        try {
            initObservables()
        } catch (e: Exception) {
            showToast("No se pudo cargar el producto")
        }
    }

    override fun initObservables() {
        ventasViewModel.productosLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewSearchProduct(it)
        })

        ventasViewModel.clientesLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewSearchClientes(it)
        })
    }

    override fun init() {
        notifyRecyclerViewCarritoItemsVentas(carrito)
        recyclerViewProductosCarrito_f_ventas.layoutManager = LinearLayoutManager(getActivity())
        recyclerViewSearchProduct_ventas.layoutManager = LinearLayoutManager(getActivity())
        recyclerViewClientes_ventas.layoutManager = LinearLayoutManager(getActivity())
    }

    override fun initListeners() {

        //TODO clicks
        radioButtonCobrar.setOnClickListener {
            modeVentaPorCobrar()
        }

        radioButtonPagado.setOnClickListener {
            modeVentaPagado()
        }

        imageViewClearEdittext.setOnClickListener {
            clearBuscador()
        }

        buttonTerminarVenta.setOnClickListener {
            val venta = generateVenta()
            if (venta != null) {
                ventasViewModel.insertVenta(venta)
            }
        }

        //TODO listeners
        scrollPadre_ventas.setOnScrollChangeListener { _: NestedScrollView, scrollX: Int, scrollY: Int, _: Int, _: Int ->

            if (scrollY > 0 || scrollY < 0 && floatingActionButtonEscaner_ventas.isShown)
                floatingActionButtonEscaner_ventas.hide()
            else
                floatingActionButtonEscaner_ventas.show()

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

        edittextCliente_venta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                filterClients(editable.toString())
                if (editable.trim().isNotEmpty()) {
                    showListClientsSearch()
                } else {
                    hideListClientsSearch()
                }

            }
        })

        edittextPagoInicial_venta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                var restante = textViewTotalVenta.text.toString().toInt()
                restante = if (editable.trim().isNotEmpty()) {
                    textViewTotalVenta.text.toString().toInt() - edittextPagoInicial_venta.text.toString().toInt()
                } else {
                    textViewTotalVenta.text.toString().toInt() - 0
                }
                showCantidadRestantePagoInicial(restante)
            }
        })

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
        mAdapterBuscarProducto = AdapterProductos(mContext, list)
        mAdapterBuscarProducto.listener = this
        recyclerViewSearchProduct_ventas.adapter = mAdapterBuscarProducto
    }

    private fun notifyRecyclerViewSearchClientes(list: List<ClienteEntity>) {
        clientes = list
        mAdapterBuscarCliente = AdapterClientes(mContext, list)
        mAdapterBuscarCliente.listenerSecond = this
        recyclerViewClientes_ventas.adapter = mAdapterBuscarCliente
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
                    if (nombre.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                        if (filterdNames.size < 2)
                            filterdNames.add(producto)
                    }
                }
            }
        mAdapterBuscarProducto.filterList(filterdNames)
        recyclerViewSearchProduct_ventas.scrollBy(0,0)
    }

    internal fun filterClients(text: String) {
        clienteSeleccionado = null

        val filterdNames: ArrayList<ClienteEntity> = ArrayList()
        if (!clientes.isNullOrEmpty())
            for (cliente in clientes) {
                val nombre = cliente.nombre
                if (nombre.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                    if (filterdNames.size < 2)
                        filterdNames.add(cliente)
                }
            }
        mAdapterBuscarCliente.filterList(filterdNames)
        recyclerViewClientes_ventas.scrollBy(0,0)
    }

    internal fun hideListProductSearch() {
        recyclerViewSearchProduct_ventas.visibility = View.GONE
    }

    internal fun showListProductSearch() {
        recyclerViewSearchProduct_ventas.visibility = View.VISIBLE
    }

    internal fun hideListClientsSearch() {
        recyclerViewClientes_ventas.visibility = View.GONE
    }

    internal fun showListClientsSearch() {
        recyclerViewClientes_ventas.visibility = View.VISIBLE
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

    private fun generateVenta(): VentaEntity? {
        return if (validateForms()) {
            val venta = VentaEntity()
            venta.productosVendidos = mAdapter.listaVendido
            venta.total = textViewTotalVenta.text.toString().toInt()
            venta.descripcion = edittextDescripcion_venta.text.toString()
            venta.formaDePago = edittextFormaPago_venta.text.toString()
            venta.idCliente = clienteSeleccionado?.id ?: -1
            venta.pagado = radioButtonPagado.isChecked

            venta
        } else {
            null
        }
    }

    private fun validateForms(): Boolean {
        return (mAdapter.listaVendido.size > 0
                && !edittextCliente_venta.text.isNullOrEmpty())
    }

    override fun onClickItem(productoEntity: ProductoEntity, position: Int) {
        addProductoCarrito(productoEntity)
        clearBuscador()
    }

    override fun onClickItemSecondListener(objects: ClienteEntity, position: Int) {
        seccionarCliente(objects)
    }

    override fun removeItem(objects: ClienteEntity, position: Int) {

    }

    fun modeVentaPorCobrar() {
        radioButtonPagado.isChecked = false
        linearLayoutContentConfigACobrar.visibility = View.VISIBLE
    }

    fun modeVentaPagado() {
        radioButtonCobrar.isChecked = false
        linearLayoutContentConfigACobrar.visibility = View.GONE
    }

    fun seccionarCliente(clienteEntity: ClienteEntity) {
        clienteSeleccionado = clienteEntity
        //Toast.makeText(mContext, "cliente seleccionado: $clienteEntity", Toast.LENGTH_SHORT).show()
        //edittextCliente_venta.setText(clienteSeleccionado?.nombre?.toUpperCase())
    }


}
