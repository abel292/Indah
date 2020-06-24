package com.android_abel.indah._view_ui.fragments.addClient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._view_model.ClientesViewModel
import com.android_abel.indah._view_ui.base.BaseFragmentRecycler
import kotlinx.android.synthetic.main.fragment_agregar_cliente.*

class AgregarClienteFragment : BaseFragmentRecycler() {

    //viewModels
    val viewModel by lazy {
        ViewModelProviders.of(this).get(ClientesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_agregar_cliente, container, false)
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
        floatingActionButtonGuardarCliente.setOnClickListener {
            if (validateForm()) {
                val cliente = generateClient()
                viewModel.guardarCliente(cliente)
                clearForm()
            }
        }
    }

    private fun validateForm(): Boolean {
        return !editTextNombreClient_addClient.text.isNullOrEmpty()
    }

    private fun generateClient(): ClienteEntity {
        val clienteEntity = ClienteEntity()
        clienteEntity.nombre = editTextNombreClient_addClient.text.toString()
        clienteEntity.celular = editTextCelularClient_addClient.text.toString()
        clienteEntity.direccion = editTextDireccionClient_addClient.text.toString()
        clienteEntity.infoAdicional = editTextInfoAdicionalClient_addClient.text.toString()
        return clienteEntity
    }

    fun clearForm() {
        editTextNombreClient_addClient.setText("")
        editTextCelularClient_addClient.setText("")
        editTextDireccionClient_addClient.setText("")
        editTextInfoAdicionalClient_addClient.setText("")
    }

}
