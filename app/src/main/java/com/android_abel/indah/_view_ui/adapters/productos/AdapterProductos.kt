package com.android_abel.indah._view_ui.adapters.productos

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_ui.adapters.ventas.OnClickItemProductoSearched

class AdapterProductos(private var list: List<ProductoEntity>) :
    RecyclerView.Adapter<AdapterProductos.ProductoViewHolder>() {

    lateinit var listener: OnClickItemProductoSearched

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductoViewHolder(
            inflater,
            parent,
            listener
        )
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto: ProductoEntity = list[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class ProductoViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        var listener: OnClickItemProductoSearched
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_view_producto_fragment_productos,
                parent,
                false
            )
        ) {

        private var textViewNombreProducto: TextView? = null
        private var imageViewProducto: ImageView? = null


        init {
            textViewNombreProducto = itemView.findViewById(R.id.textViewNombreProducto)
            imageViewProducto = itemView.findViewById(R.id.imageViewProducto)
        }

        fun bind(producto: ProductoEntity) {
            textViewNombreProducto?.text = producto.nombre
            itemView.setOnClickListener {
                listener.onClickItemProductoSearched(producto)
            }

        }

    }

    fun filterList(filterdNames: List<ProductoEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}