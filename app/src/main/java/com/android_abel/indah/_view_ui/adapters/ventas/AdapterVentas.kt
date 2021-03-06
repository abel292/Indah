package com.android_abel.indah._view_ui.adapters.ventas

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity


class AdapterVentas(private var list: ArrayList<ProductoEntity>, private var context: Context, var recyclerView: RecyclerView?) :
    RecyclerView.Adapter<AdapterVentas.VentasViewHolder>() {

    lateinit var listenerCarrito: ListenerCarrito
    lateinit var listenerConfigVenta: ConfigVentaListener

    //
    var listaVendido = ArrayList<ProductoVendidoEntity>()

    init {
        list.forEach {
            it.id?.let { id -> ProductoVendidoEntity(id, 1, it.precioVenta, 0) }?.let { vendidoEntity ->
                listaVendido.add(0, vendidoEntity) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentasViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return VentasViewHolder(
            inflater,
            parent,
            context,
            list,
            listaVendido,
            listenerCarrito,
            listenerConfigVenta
        )
    }

    fun addProducto(producto: ProductoEntity) {
        list.add(0, producto)
        producto.id?.let { id -> ProductoVendidoEntity(id, 1, producto.precioVenta, 0) }?.let { listaVendido.add(0, it) }
        notifyItemInserted(0)
        recyclerView?.scrollBy(0, 0)
    }

    fun deleteProducto(producto: ProductoEntity, position: Int) {
        list.remove(producto)
        listaVendido.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
        recyclerView?.scrollBy(0, 0)

    }

    fun filterList(filterdNames: ArrayList<ProductoEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VentasViewHolder, position: Int) {
        val producto: ProductoEntity = list[position]
        holder.setIsRecyclable(false)
        if (!list.isNullOrEmpty()) {
            holder.bind(producto, position)
        }
    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class VentasViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        var context: Context,
        var list: ArrayList<ProductoEntity>,
        var listVendidoEntity: ArrayList<ProductoVendidoEntity>,
        var listenerCarrito: ListenerCarrito,
        var configVentaListener: ConfigVentaListener
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_view_venta_producto_fragment_ventas,
                parent,
                false
            )
        ) {
        private var textViewNombreProducto_itemVenta: TextView? = null
        private var textViewSubTotal: TextView? = null
        private var imageViewVentas_itemVentas: ImageView? = null
        private var imageButtonRemoveItemCarrito: ImageView? = null
        private var editTextCantidad_ventas: EditText? = null
        private var editTextPrecioVenta_ventas: EditText? = null

        /*private var cantidad = 1
        private var precioVenta = 0*/

        init {
            textViewNombreProducto_itemVenta = itemView.findViewById(R.id.textViewNombreProducto_itemVenta)
            textViewSubTotal = itemView.findViewById(R.id.textViewSubTotal)
            imageViewVentas_itemVentas = itemView.findViewById(R.id.imageViewVentas_itemVentas)
            imageButtonRemoveItemCarrito = itemView.findViewById(R.id.imageButtonRemoveItemCarrito)
            editTextCantidad_ventas = itemView.findViewById(R.id.editTextCantidad_ventas)
            editTextPrecioVenta_ventas = itemView.findViewById(R.id.editTextPrecioVenta_ventas)
        }

        fun bind(producto: ProductoEntity, position: Int) {
            textViewNombreProducto_itemVenta?.text = producto.nombre

            imageButtonRemoveItemCarrito?.setOnClickListener {
                listenerCarrito.removeItem(producto, position)
            }
            editTextCantidad_ventas?.setText(listVendidoEntity[position].cantidad.toString())
            editTextPrecioVenta_ventas?.setText(listVendidoEntity[position].precioVenta.toString())

            producto.id?.let { id ->
                setSubTotal(
                    id,
                    editTextCantidad_ventas?.text.toString().toInt(),
                    editTextPrecioVenta_ventas?.text.toString().toInt()
                )
            }

            editTextCantidad_ventas?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {

                    //modifico este para guardarlo en productos vendidos
                    if (editable.toString().trim().isNotEmpty() &&
                        editable.toString().trim().isNotBlank()
                    ) {
                        producto.id?.let { id ->
                            try {
                                setSubTotal(
                                    id,
                                    editTextCantidad_ventas?.text.toString().toInt(),
                                    editTextPrecioVenta_ventas?.text.toString().toInt()
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, context.getString(R.string.error_al_calcular), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })

            editTextPrecioVenta_ventas?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {

                    //modifico este para guardarlo en productos vendidos
                    if (editable.toString().trim().isNotEmpty() &&
                        editable.toString().trim().isNotBlank()
                    ) {
                        producto.id?.let { id ->
                            try {
                                setSubTotal(
                                    id,
                                    editTextCantidad_ventas?.text.toString().toInt(),
                                    editTextPrecioVenta_ventas?.text.toString().toInt()
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, context.getString(R.string.error_al_calcular), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
            })

            //enviamos la lista de productos ya modificada al fragment cada vez que se bindeé los items del recicler
        }

        fun setSubTotal(idProducto: Int, cant: Int, price: Int) {
            val subTotal = price * cant
            textViewSubTotal?.text = subTotal.toString()

            //modifico este para guardarlo en productos vendidos
            listVendidoEntity.forEach {
                if (idProducto == it.idProducto) {
                    it.subTotal = textViewSubTotal?.text.toString().toInt()
                    it.cantidad = editTextCantidad_ventas?.text.toString().toInt()
                    it.precioVenta = editTextPrecioVenta_ventas?.text.toString().toInt()
                    return@forEach
                }
            }
            generateVentas()
        }

        private fun generateVentas() {
            configVentaListener.compilandoProductosCarrito(listVendidoEntity)
        }
        //PEQUEÑO PROBLEMA QUE NO ACTUALIZA EL SUBTOTAL CUANDO MODIFICO LUEGO DE AGREGAR UN 2DO PRODUCTO
    }

}