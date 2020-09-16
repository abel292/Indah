package com.android_abel.indah._view_ui.adapters.productos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_ventas.*

class AdapterProductos(var context: Context, lista: List<ProductoEntity>, var recyclerView: RecyclerView) :
    BaseAdapterRecycler<ProductoEntity>() {

    lateinit var listener: OnListenerItemRecyclerView<ProductoEntity>
    var list = lista

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
        holder.bind(producto, position)
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

        override fun bind(objeto: ProductoEntity, position: Int) {
            val name: String = (objeto.nombre?.substring(0, 1)?.toUpperCase() ?: "") + (objeto.nombre?.substring(1)?.toLowerCase() ?: "")
            textViewNombreProducto?.text = name
            textViewPrecio_item_producto?.text = objeto.precioVenta.toString()
            textViewCantidad_item_producto?.text = objeto.cantidad.toString()
            textViewDescripcion_item_producto?.text = objeto.descripcion ?: ""

            imageViewProducto?.let {
                Glide.with(context)
                    .load(objeto.urlImagen)
                    .placeholder(R.drawable.ic_scan)
                    .error(R.drawable.ic_scan)
                    .override(200, 200).centerCrop().into(it)
            }
            itemView.setOnClickListener {
                listener.onClickItem(objeto, position)
            }

        }

    }

    fun filterList(filterdNames: List<ProductoEntity>) {
        this.list = filterdNames
        recyclerView.scrollBy(0, 0)
        recyclerView.visibility = View.VISIBLE
        notifyDataSetChanged()

    }

}