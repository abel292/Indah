package com.android_abel.indah._view_ui.adapters.historialVentas

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.venta.VentaEntity


class AdapterHistorial(private var list: List<VentaEntity>) :
    RecyclerView.Adapter<AdapterHistorial.HistorialVentasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialVentasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HistorialVentasViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: HistorialVentasViewHolder, position: Int) {
        val venta: VentaEntity = list[position]
        holder.bind(venta)
    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class HistorialVentasViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_view_historial_ventas_fragment_historial,
                parent,
                false
            )
        ) {

        private var textViewFechaVenta_historial: TextView? = null
        private var textViewCliente_hsitorial: TextView? = null
        private var textViewListProductos_historial: TextView? = null
        private var textViewTotal_historial: TextView? = null

        init {
            textViewFechaVenta_historial = itemView.findViewById(R.id.textViewFechaVenta_historial)
            textViewCliente_hsitorial = itemView.findViewById(R.id.textViewCliente_hsitorial)
            textViewListProductos_historial = itemView.findViewById(R.id.textViewListProductos_historial)
            textViewTotal_historial = itemView.findViewById(R.id.textViewTotal_historial)
        }

        fun bind(venta: VentaEntity) {
            textViewCliente_hsitorial?.text = venta.idCliente.toString()
            textViewTotal_historial?.text = venta.total.toString()
            var cadena = ""
            venta.productosVendidos?.forEach { _producto ->
                cadena = if (cadena.isEmpty())
                    _producto.idProducto.toString()
                else
                    "$cadena, " + _producto.idProducto.toString()
            }
            textViewListProductos_historial?.text = cadena

        }

    }

    fun filterList(filterdNames: List<VentaEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}