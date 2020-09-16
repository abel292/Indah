package com.android_abel.indah._view_ui.adapters.historialVentas

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_ui.adapters.base.BaseAdapterRecycler
import com.android_abel.indah._view_ui.adapters.base.BaseAdapterRecyclerTwo
import com.android_abel.indah._view_ui.adapters.base.HolderBase
import com.android_abel.indah._view_ui.adapters.base.HolderBaseTwo
import kotlinx.android.synthetic.main.fragment_historial_ventas.*
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


        }

    }

    fun filterList(filterdNames: List<VentaEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}