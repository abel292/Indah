package com.android_abel.indah._view_ui.adapters.porCobrar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_ui.adapters.base.BaseAdapterRecycler
import com.android_abel.indah._view_ui.adapters.base.HolderBase
import com.android_abel.indah._view_ui.adapters.ventas.OnListenerItemRecyclerView

class AdapterVentaPorCobrar(var context: Context, private var list: List<VentaEntity>) :
    BaseAdapterRecycler<VentaEntity>() {

    lateinit var listener: OnListenerItemRecyclerView<VentaEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaPorCobrarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VentaPorCobrarViewHolder(
            inflater,
            parent,
            context,
            listener
        )
    }

    override fun onBindViewHolder(holder: HolderBase<VentaEntity>, position: Int) {
        val producto: VentaEntity = list[position]
        holder.bind(producto, position)
    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class VentaPorCobrarViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        var context: Context,
        var listener: OnListenerItemRecyclerView<VentaEntity>
    ) :
        HolderBase<VentaEntity>(
            inflater, parent, R.layout.item_venta_por_cobrar

        ) {
        val textViewFecha = itemView.findViewById(R.id.textViewFecha) as TextView
        val textViewCliente = itemView.findViewById(R.id.textViewCliente) as TextView
        val textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion) as TextView
        val textViewTotal = itemView.findViewById(R.id.textViewTotal) as TextView
        val textViewPagos = itemView.findViewById(R.id.textViewPagos) as TextView
        val textViewDeuda = itemView.findViewById(R.id.textViewDeuda) as TextView

        override fun bind(ventaEntity: VentaEntity, position: Int) {

            itemView.setOnClickListener {
                listener.onClickItem(ventaEntity, position)
            }

            textViewFecha.text = ventaEntity.fecha.toString()
            textViewCliente.text = ventaEntity.idCliente.toString()
            textViewDescripcion.text = ventaEntity.descripcion ?: ""
            textViewTotal.text = ventaEntity.total.toString()
            textViewPagos.text = ventaEntity.pagoInicial.toString()

            val pagos = ventaEntity.pagoInicial ?: 0
            val deuda = ventaEntity.total?.minus(pagos) ?: 0
            textViewDeuda.text = deuda.toString()
        }

    }

    fun filterList(filterdNames: List<VentaEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}