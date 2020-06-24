package com.android_abel.indah._view_ui.adapters.clientes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._view_ui.adapters.base.BaseAdapterRecycler
import com.android_abel.indah._view_ui.adapters.base.HolderBase
import com.android_abel.indah._view_ui.adapters.ventas.OnListenerItemRecyclerView
import com.android_abel.indah._view_ui.adapters.ventas.OnSecondListenerItemRecyclerView

class AdapterClientes(var context: Context, var list: List<ClienteEntity>) : BaseAdapterRecycler<ClienteEntity>() {
    var listener: OnListenerItemRecyclerView<ClienteEntity>? = null
    var listenerSecond: OnSecondListenerItemRecyclerView<ClienteEntity>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ClienteHolder(
            inflater,
            parent,
            context,
            listener, listenerSecond
        )
    }


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HolderBase<ClienteEntity>, position: Int) {
        val cliente: ClienteEntity = list[position]
        holder.bind(cliente, position)
    }


    //todo Holder
    class ClienteHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        var context: Context,
        var listener: OnListenerItemRecyclerView<ClienteEntity>?,
        var listenerSecond: OnSecondListenerItemRecyclerView<ClienteEntity>?
    ) :
        HolderBase<ClienteEntity>(inflater, parent, R.layout.item_view_client_fragment_client) {

        private var textViewCliente_cliente: TextView? = null
        private var textViewDescripcion_cliente: TextView? = null

        init {
            textViewCliente_cliente = itemView.findViewById(R.id.textViewCliente_cliente)
            textViewDescripcion_cliente = itemView.findViewById(R.id.textViewDescripcion_cliente)

        }

        override fun bind(client: ClienteEntity, position: Int) {
            textViewCliente_cliente?.text = client.nombre
            textViewDescripcion_cliente?.text = client.infoAdicional
            itemView.setOnClickListener {
                listener?.onClickItem(client, position)
                listenerSecond?.onClickItemSecondListener(client, position)
            }
        }

    }

    fun filterList(filterdNames: List<ClienteEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}