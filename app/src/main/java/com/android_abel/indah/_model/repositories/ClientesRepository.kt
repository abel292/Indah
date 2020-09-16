package com.android_abel.indah._model.repositories

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android_abel.indah._model.local.DataBaseIndah
import com.android_abel.indah._model.local.cliente.ClienteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClientesRepository(context: Context) {

    var database = DataBaseIndah.getDatabase(context)
    var dao = database.clientesDao()

    val clientesLive: LiveData<List<ClienteEntity>> = dao.getAllLive()


    @WorkerThread
    suspend fun insert(clienteEntity: ClienteEntity) = withContext(Dispatchers.IO) {
        dao.insert(clienteEntity)
    }

    @WorkerThread
    suspend fun getClienteWithID(id: Int): ClienteEntity? = withContext(Dispatchers.Default)
    {
        return@withContext dao.getSingle(id)
    }


    @WorkerThread
    suspend fun getAll(): List<ClienteEntity>? = withContext(Dispatchers.Default)
    {
        return@withContext dao.getAll()
    }
}