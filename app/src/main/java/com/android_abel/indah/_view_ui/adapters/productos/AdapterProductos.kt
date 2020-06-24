package com.android_abel.indah._view_ui.adapters.productos

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_ui.adapters.base.BaseAdapterRecycler
import com.android_abel.indah._view_ui.adapters.base.HolderBase
import com.android_abel.indah._view_ui.adapters.ventas.OnListenerItemRecyclerView
import com.bumptech.glide.Glide

class AdapterProductos(var context: Context, private var list: List<ProductoEntity>) :
    BaseAdapterRecycler<ProductoEntity>() {

    lateinit var listener: OnListenerItemRecyclerView<ProductoEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductoViewHolder(
            inflater,
            parent,
            context,
            listener
        )
    }

    override fun onBindViewHolder(holder: HolderBase<ProductoEntity>, position: Int) {
        val producto: ProductoEntity = list[position]
        holder.bind(producto,position)
    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class ProductoViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        var context: Context,
        var listener: OnListenerItemRecyclerView<ProductoEntity>
    ) :
        HolderBase<ProductoEntity>(
            inflater, parent, R.layout.item_view_producto_fragment_productos

            ) {

        private var textViewNombreProducto: TextView? = null
        private var textViewDescripcion_item_producto: TextView? = null
        private var textViewPrecio_item_producto: TextView? = null
        private var textViewCantidad_item_producto: TextView? = null
        private var imageViewProducto: ImageView? = null


        init {
            textViewNombreProducto = itemView.findViewById(R.id.textViewNombreProducto)
            textViewDescripcion_item_producto = itemView.findViewById(R.id.textViewDescripcion_item_producto)
            textViewPrecio_item_producto = itemView.findViewById(R.id.textViewPrecio_item_producto)
            textViewCantidad_item_producto = itemView.findViewById(R.id.textViewCantidad_item_producto)

            imageViewProducto = itemView.findViewById(R.id.imageViewProducto)
        }

        override fun bind(producto: ProductoEntity, position: Int) {
            textViewNombreProducto?.text = producto.nombre
            textViewPrecio_item_producto?.text = producto.precioVenta.toString()
            textViewCantidad_item_producto?.text = producto.cantidad.toString()
            textViewDescripcion_item_producto?.text = producto.descripcion ?: ""

            imageViewProducto?.let {
                Glide.with(context)
                    .load(context.getString(R.string.urlEjemplo))
                    .placeholder(R.drawable.ic_scan)
                    .error(R.drawable.ic_scan)
                    .override(200, 200).centerCrop().into(it)
            }
            itemView.setOnClickListener {
                listener.onClickItem(producto,position)
            }

        }

    }

    fun filterList(filterdNames: List<ProductoEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}