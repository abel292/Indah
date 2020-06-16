package com.android_abel.indah._view_ui.adapters.ventas

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.ProductoVendido
import com.google.android.material.textfield.TextInputEditText

class AdapterVentas(private var list: ArrayList<ProductoEntity>) :
    RecyclerView.Adapter<AdapterVentas.VentasViewHolder>() {

    lateinit var listenerCarrito: ListenerCarrito
    lateinit var listenerConfigVenta: ConfigVentaListener

    //
    var listaVendido = ArrayList<ProductoVendido>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VentasViewHolder(
            inflater,
            parent,
            list,
            listaVendido,
            listenerCarrito,
            listenerConfigVenta
        )
    }

    fun addProducto(producto: ProductoEntity) {
        list.add(0, producto)
        listaVendido.add(0, ProductoVendido(producto.id, 1, producto.precioVenta, 0))
        notifyItemInserted(0)
    }

    fun deleteProducto(producto: ProductoEntity, position: Int) {
        list.remove(producto)
        listaVendido.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun filterList(filterdNames: ArrayList<ProductoEntity>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VentasViewHolder, position: Int) {
        val producto: ProductoEntity = list[position]
        holder.setIsRecyclable(false)
        holder.bind(producto, position)

    }

    override fun getItemCount(): Int = list.size

    //todo Holder
    class VentasViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        var list: ArrayList<ProductoEntity>,
        var listVendido: ArrayList<ProductoVendido>,
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
        private var imageButtonRemoveItemCarrito: ImageButton? = null
        private var editTextCantidad_ventas: TextInputEditText? = null
        private var editTextPrecioVenta_ventas: TextInputEditText? = null

        private var cantidad = 1
        private var precioVenta = 0

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
            precioVenta = producto.precioVenta

            editTextCantidad_ventas?.setText(cantidad.toString())
            editTextPrecioVenta_ventas?.setText(precioVenta.toString())

            setSubTotal(position, cantidad, precioVenta)

            editTextCantidad_ventas?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {

                    cantidad = if (editable.toString().trim().isNotEmpty()) editable.toString().toInt() else 1
                    setSubTotal(position, cantidad, precioVenta)

                    //modifico este para guardarlo en productos vendidos
                    listVendido[position].cantidad = cantidad

                    generateVentas()
                }
            })

            editTextPrecioVenta_ventas?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {

                    precioVenta = if (editable.toString().trim().isNotEmpty()) editable.toString().toInt() else producto.precioVenta
                    setSubTotal(position, cantidad, precioVenta)

                    //modifico este para guardarlo en productos vendidos
                    listVendido[position].precioVenta = precioVenta

                    generateVentas()
                }
            })

            //enviamos la lista de productos ya modificada al fragment cada vez que se bindeé los items del recicler
            generateVentas()
        }

        fun setSubTotal(position: Int, cant: Int, price: Int) {
            val subTotal = price * cant
            textViewSubTotal?.text = subTotal.toString()

            //modifico este para guardarlo en productos vendidos
            listVendido[position].subTotal = subTotal
        }

        fun generateVentas() {
            configVentaListener.compilarProductosCarrito(listVendido)
        }

        //PEQUEÑO PROBLEMA QUE NO ACTUALIZA EL SUBTOTAL CUANDO MODIFICO LUEGO DE AGREGAR UN 2DO PRODUCTO
    }

}