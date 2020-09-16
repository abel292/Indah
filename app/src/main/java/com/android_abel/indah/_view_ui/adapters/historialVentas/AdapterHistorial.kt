package com.android_abel.indah._view_ui.adapters.historialVentas

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_ui.adapters.base.BaseAdapterRecyclerTwo
import com.android_abel.indah._view_ui.adapters.base.HolderBaseTwo
import java.text.SimpleDateFormat
import java.util.*


class AdapterHistorial(
    private var context: Context,
    private var list: List<VentaEntity>, private var listClientes: List<ClienteEntity>
) :
    BaseAdapterRecyclerTwo<VentaEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialVentasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HistorialVentasViewHolder(
            inflater,
            parent,
            context
        )
    }

    override fun onBindViewHolder(holder: HolderBaseTwo<VentaEntity>, position: Int) {
        val venta: VentaEntity = list[position]

        var clienteEntity = ClienteEntity()

        listClientes.forEach {
            if (it.id == venta.idCliente) {
                clienteEntity = it
                return@forEach
            }
        }
        holder.bind(venta, clienteEntity, position)
    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class HistorialVentasViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        var context: Context
    ) :
        HolderBaseTwo<VentaEntity>(
            inflater, parent,
            R.layout.item_view_historial_ventas_fragment_historial
        ) {

        private var textViewFechaVenta_historial: TextView? = null
        private var textViewCliente_hsitorial: TextView? = null
        private var textViewListProductos_historial: TextView? = null
        private var textViewTotal_historial: TextView? = null
        private var textViewDescripcion: TextView? = null
        private var recyclerViewListaProductos: RecyclerView? = null

        init {
            textViewFechaVenta_historial = itemView.findViewById(R.id.textViewFechaVenta_historial)
            textViewCliente_hsitorial = itemView.findViewById(R.id.textViewCliente_hsitorial)
            textViewListProductos_historial = itemView.findViewById(R.id.textViewListProductos_historial)
            textViewTotal_historial = itemView.findViewById(R.id.textViewTotal_historial)
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion)

            recyclerViewListaProductos = itemView.findViewById(R.id.recyclerViewListaProductos)
            recyclerViewListaProductos?.layoutManager = LinearLayoutManager(context);
        }

        override fun bind(venta: VentaEntity, position: Int) {
        }

        override fun bind(venta: VentaEntity, cliente: ClienteEntity, position: Int) {


            textViewCliente_hsitorial?.text = venta.idCliente.toString()
            textViewTotal_historial?.text = venta.total.toString()
            var cadena = ""
            venta.productosVendidoEntities?.forEach { _producto ->
                cadena = if (cadena.isEmpty())
                    _producto.nameProducto
                else
                    "$cadena, " + _producto.nameProducto
            }
            textViewListProductos_historial?.text = cadena

            val dateVenta = venta.fecha
            var fecha = ""
            dateVenta?.let {
                val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                fecha = df.format(dateVenta)
            }
            textViewCliente_hsitorial?.text = cliente.nombre ?: ""
            textViewFechaVenta_historial?.text = fecha
            textViewDescripcion?.text = venta.descripcion ?: ""

            val adapterListaProductos = recyclerViewListaProductos?.let { recyclerViewListaProductos ->
                venta.productosVendidoEntities?.let { listaVendidos ->
                    AdapterListaProductos(
                        listaVendidos,
                        recyclerViewListaProductos
                    )
                }
            }
            recyclerViewListaProductos?.adapter = adapterListaProductos

            itemView.setOnClickListener {
                if (recyclerViewListaProductos?.visibility == ViewGroup.VISIBLE) {
                    textViewDescripcion?.let { it1 -> viewGoneAnimator(it1) }
                    recyclerViewListaProductos?.let { it1 -> viewGoneAnimator(it1) }
                } else {
                    textViewDescripcion?.let { it1 -> viewVisibleAnimator(it1) }
                    recyclerViewListaProductos?.let { it1 -> viewVisibleAnimator(it1) }
                }
            }

        }

        private fun viewGoneAnimator(view: View) {
            view.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = View.GONE
                    }
                })
        }

        private fun viewVisibleAnimator(view: View) {
            view.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = VISIBLE
                    }
                })
        }

    }


    fun filterList(filterdNames: List<VentaEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}