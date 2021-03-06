package com.android_abel.indah._view_ui.fragments.configVenta

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_model.VentasViewModel
import com.android_abel.indah._view_ui.adapters.clientes.AdapterClientes
import com.android_abel.indah._view_ui.adapters.ventas.OnSecondListenerItemRecyclerView
import com.android_abel.indah._view_ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_config_venta.*
import java.util.*
import kotlin.collections.ArrayList

class ConfigVentaFragment : BaseFragment(), OnSecondListenerItemRecyclerView<ClienteEntity> {

    lateinit var mContext: Context
    val ventasViewModel by lazy {
        ViewModelProviders.of(this).get(VentasViewModel::class.java)
    }

    lateinit var mAdapterBuscarCliente: AdapterClientes
    var clienteSeleccionado: ClienteEntity? = null
    lateinit var clientes: List<ClienteEntity>
    lateinit var venta: VentaEntity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_config_venta, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArgument()
        initObservables()
        init()
        initListeners()
    }

    fun initArgument(){

    }

    override fun initObservables() {
        ventasViewModel.clientesLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewSearchClientes(it)
        })
    }

    override fun init() {
        recyclerViewClientes_ventas.layoutManager = LinearLayoutManager(mContext)


    }

    override fun initListeners() {

        //TODO clicks
        radioButtonCobrar.setOnClickListener {
            modeVentaPorCobrar()
        }

        radioButtonPagado.setOnClickListener {
            modeVentaPagado()
        }

        buttonTerminarVenta.setOnClickListener {
            val venta = generateVenta()
            if (venta != null) {
                ventasViewModel.insertVenta(venta)
            }
        }

        radioEfectivo.setOnClickListener {
            checkButton(it)
        }

        radioTarjeta.setOnClickListener {
            checkButton(it)
        }

        radioOtras.setOnClickListener {
            checkButton(it)
        }


        imageViewSelectFormaPago.setOnClickListener {
            if (contentSelectFormaPago.visibility == View.VISIBLE) {
                contentSelectFormaPago.visibility = View.GONE
            } else {
                contentSelectFormaPago.visibility = View.VISIBLE
            }

        }

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

    override fun onClickItemSecondListener(objects: ClienteEntity, position: Int) {
        seccionarCliente(objects)
    }

    override fun removeItem(objects: ClienteEntity, position: Int) {

    }


    private fun notifyRecyclerViewSearchClientes(list: List<ClienteEntity>) {
        clientes = list
        mAdapterBuscarCliente = AdapterClientes(mContext, list, recyclerViewClientes_ventas)
        mAdapterBuscarCliente.listenerSecond = this
        recyclerViewClientes_ventas.adapter = mAdapterBuscarCliente
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
    }


    fun checkButton(v: View?) {
        val radioId = radioGroup.checkedRadioButtonId
        val radioButton = fragmentView.findViewById<RadioButton>(radioId)
        Toast.makeText(mContext, radioButton.text, Toast.LENGTH_SHORT).show()
    }

    internal fun hideListClientsSearch() {
        recyclerViewClientes_ventas.visibility = View.GONE
    }

    internal fun showListClientsSearch() {
        recyclerViewClientes_ventas.visibility = View.VISIBLE
    }

    internal fun showCantidadRestantePagoInicial(cant: Int) {
        textViewDeudaRestante.text = cant.toString()
    }

    private fun generateVenta(): VentaEntity? {
        return if (validateForms()) {
            venta = VentaEntity()
            //falta la lista de vendidos
            //venta.productosVendidos = mAdapter.listaVendido
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
        return (!edittextCliente_venta.text.isNullOrEmpty())
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
        Toast.makeText(mContext, "cliente seleccionado: $clienteEntity", Toast.LENGTH_SHORT).show()
        //edittextCliente_venta.setText(clienteSeleccionado?.nombre?.toUpperCase())
    }
}