package com.android_abel.indah._view_ui.fragments.historialVentas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_model.VentasViewModel
import com.android_abel.indah._view_ui.adapters.historialVentas.AdapterHistorial
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import kotlinx.android.synthetic.main.fragment_historial_ventas.*

class HistorialVentasFragment : BaseFragment(), BasicMethods {
    //viewModels
    val ventasViewModel by lazy {
        ViewModelProviders.of(this).get(VentasViewModel::class.java)
    }

    //adapters
    lateinit var mAdapter: AdapterHistorial

    //list
    lateinit var allVentas: List<VentaEntity>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_historial_ventas, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
        ventasViewModel.ventasLive.observe(viewLifecycleOwner, Observer { listVentas ->
            listVentas.sortedByDescending { it.fecha }
            allVentas = listVentas
            ventasViewModel.getAllCliente()
        })

        ventasViewModel.clientes.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewItems(it)
        })
    }

    override fun init() {
        recyclerViewHistorialVentas.layoutManager = LinearLayoutManager(getActivity());
    }

    override fun initListeners() {
    }

    private fun notifyRecyclerViewItems(clientes: ArrayList<ClienteEntity>) {
        mAdapter =
            AdapterHistorial(
                requireContext(),
                allVentas, clientes
            )
        recyclerViewHistorialVentas.adapter = mAdapter
    }

}
