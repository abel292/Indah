package com.android_abel.indah._view_ui.adapters.historialVentas

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import com.android_abel.indah._view_ui.adapters.base.BaseAdapterRecycler
import com.android_abel.indah._view_ui.adapters.base.HolderBase
import com.android_abel.indah._view_ui.adapters.ventas.OnListenerItemRecyclerView
import com.bumptech.glide.Glide

class AdapterListaProductos(lista: List<ProductoVendidoEntity>, var recyclerView: RecyclerView) :
    BaseAdapterRecycler<ProductoVendidoEntity>() {

    //lateinit var listener: OnListenerItemRecyclerView<ProductoEntity>
    var list = lista

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductoViewHolder(
            inflater,
            parent/*,
            listener*/
        )
    }

    override fun onBindViewHolder(holder: HolderBase<ProductoVendidoEntity>, position: Int) {
        val producto: ProductoVendidoEntity = list[position]
        holder.bind(producto, position)
    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class ProductoViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
        /*var listener: OnListenerItemRecyclerView<ProductoEntity>*/
    ) :
        HolderBase<ProductoVendidoEntity>(
            inflater, parent, R.layout.item_lista_productos_vendidos

        ) {

        private var textViewNombreProducto: TextView? = null
        private var textViewDescripcion_item_producto: TextView? = null
        private var TextViewCantidadVendido: TextView? = null


        init {
            textViewNombreProducto = itemView.findViewById(R.id.textViewNombreProducto)
            textViewDescripcion_item_producto = itemView.findViewById(R.id.textViewDescripcion_item_producto)
            TextViewCantidadVendido = itemView.findViewById(R.id.TextViewCantidadVendido)
        }

        override fun bind(objeto: ProductoVendidoEntity, position: Int) {
            val name: String = (objeto.nameProducto?.substring(0, 1)?.toUpperCase() ?: "") + (objeto.nameProducto?.substring(1)?.toLowerCase() ?: "")
            textViewNombreProducto?.text = name
            TextViewCantidadVendido?.text = objeto.cantidad.toString()

            itemView.setOnClickListener {
                Log.e("click", name)
                //listener.onClickItem(objeto, position)
            }

        }

    }

    fun filterList(filterdNames: List<ProductoVendidoEntity>) {
        this.list = filterdNames
        recyclerView.scrollBy(0, 0)
        recyclerView.visibility = View.VISIBLE
        notifyDataSetChanged()

    }

}