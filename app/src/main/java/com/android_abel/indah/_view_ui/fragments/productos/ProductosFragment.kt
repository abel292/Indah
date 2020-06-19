package com.android_abel.indah._view_ui.fragments.productos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_model.ProductosViewModel
import com.android_abel.indah._view_ui.adapters.productos.AdapterProductos
import com.android_abel.indah._view_ui.adapters.ventas.OnClickItemProductoSearched
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import kotlinx.android.synthetic.main.fragment_productos.*

class ProductosFragment : BaseFragment(), BasicMethods, OnClickItemProductoSearched {

    //view
    lateinit var productosView: View

    //adapters
    lateinit var mAdapter: AdapterProductos

    //list
    lateinit var productos: List<ProductoEntity>

    //viewModels
    val productoViewModel by lazy {
        ViewModelProviders.of(this).get(ProductosViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productosView = inflater.inflate(R.layout.fragment_productos, container, false)
        return productosView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
        productoViewModel.productosLive.observe(viewLifecycleOwner, Observer {
            notifyRecyclerViewItems(it)
        })
    }

    override fun init() {
        recyclerViewProductos.layoutManager = LinearLayoutManager(getActivity())
    }

    override fun initListeners() {
        floatingActionButtonAddProducto.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_productosFragment_to_creacionProyectoFragment)
        }

        autoCompleteTextViewProductos.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString())
            }
        })
    }

    override fun onClickItemProductoSearched(productoEntity: ProductoEntity) {

    }

    private fun notifyRecyclerViewItems(list: List<ProductoEntity>) {
        productos = list
        mAdapter =
            AdapterProductos(
                list
            )
        mAdapter.listener = this
        recyclerViewProductos.adapter = mAdapter
    }

    private fun filter(text: String) {
        val filterdNames: ArrayList<ProductoEntity> = ArrayList()

        for (producto in productos) {
            val nombre = producto.nombre
            if (nombre != null) {
                if (nombre.toLowerCase().contains(text.toLowerCase())) {
                    filterdNames.add(producto)
                }
            }
        }
        mAdapter.filterList(filterdNames)
    }


}
