package com.android_abel.indah._view_model

import android.app.Application
import androidx.lifecycle.LiveData
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.repositories.ClientesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClientesViewModel(application: Application) : BaseViewModel(application) {

    val clientesRepository = ClientesRepository(application)
    val clientesLive: LiveData<List<ClienteEntity>> = clientesRepository.clientesLive

    fun guardarCliente(clienteEntity: ClienteEntity) {
        GlobalScope.launch {
            clientesRepository.insert(clienteEntity)
        }
    }
}