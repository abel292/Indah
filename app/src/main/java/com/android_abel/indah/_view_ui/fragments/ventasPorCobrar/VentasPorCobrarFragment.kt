package com.android_abel.indah._view_ui.fragments.ventasPorCobrar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.android_abel.indah.R
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_model.VentasViewModel
import com.android_abel.indah._view_ui.adapters.porCobrar.AdapterVentaPorCobrar
import com.android_abel.indah._view_ui.adapters.ventas.OnListenerItemRecyclerView
import com.android_abel.indah._view_ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_ventas_por_cobrar.*

class VentasPorCobrarFragment : BaseFragment(), OnListenerItemRecyclerView<VentaEntity> {

    //viewModels
    val ventasViewModel by lazy {
        ViewModelProviders.of(this).get(VentasViewModel::class.java)
    }

    //list
    lateinit var allVentas: List<VentaEntity>

    //variables
    lateinit var mContext: Context

    //adapters
    lateinit var mAdapter: AdapterVentaPorCobrar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_ventas_por_cobrar, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireContext()
        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
        ventasViewModel.ventasLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewItems(it)
        })
    }

    override fun init() {
        recyclerVentasPorCobrar.layoutManager = LinearLayoutManager(getActivity())
    }

    override fun initListeners() {
    }

    private fun notifyRecyclerViewItems(list: List<VentaEntity>) {
        allVentas = list
        mAdapter =
            AdapterVentaPorCobrar(mContext, list)
        mAdapter.listener = this
        recyclerVentasPorCobrar.adapter = mAdapter
    }

    override fun onClickItem(objects: VentaEntity, position: Int) {
        fragmentView.goToWithVenta(R.id.action_ventasPorCobrarFragment_to_visualizadorVentasFragment, objects)

    }
}
