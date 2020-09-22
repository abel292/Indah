package com.android_abel.indah._view_ui.fragments.clientes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._view_model.ClientesViewModel
import com.android_abel.indah._view_model.CreacionProductoViewModel
import com.android_abel.indah._view_ui.adapters.clientes.AdapterClientes
import com.android_abel.indah._view_ui.adapters.ventas.OnListenerItemRecyclerView
import com.android_abel.indah._view_ui.adapters.ventas.OnSecondListenerItemRecyclerView
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import kotlinx.android.synthetic.main.fragment_clientes.*

class ClientesFragment : BaseFragment(), BasicMethods, OnListenerItemRecyclerView<ClienteEntity>, OnSecondListenerItemRecyclerView<ClienteEntity> {

    //adapters
    lateinit var mAdapter: AdapterClientes

    //list
    lateinit var clientes: List<ClienteEntity>

    //viewModels
    val viewModel by lazy {
        ViewModelProviders.of(this).get(ClientesViewModel::class.java)
    }


    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_clientes, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = this.requireContext()

        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
        viewModel.clientesLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewItems(it)
        })
    }

    override fun init() {
        recyclerViewClientes.layoutManager = LinearLayoutManager(getActivity())
    }

    override fun initListeners() {
        floatingActionButtonAddClient.setOnClickListener {
            it.goTo(R.id.action_clientesFragment_to_agregarClienteFragment)
        }
    }

    private fun notifyRecyclerViewItems(list: List<ClienteEntity>) {
        clientes = list
        mAdapter = AdapterClientes(mContext, list, recyclerViewClientes)
        mAdapter.listener = this
        mAdapter.listenerSecond = this
        recyclerViewClientes.adapter = mAdapter
    }

    override fun onClickItem(objects: ClienteEntity, position: Int) {

    }

    override fun onClickItemSecondListener(objects: ClienteEntity, position: Int) {

    }

    override fun removeItem(objects: ClienteEntity, position: Int) {
        showToast("clic : $objects")
        viewModel.delete(objects)
    }

}

