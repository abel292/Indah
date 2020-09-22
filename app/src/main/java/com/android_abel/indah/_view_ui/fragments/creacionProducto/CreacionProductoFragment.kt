package com.android_abel.indah._view_ui.fragments.creacionProducto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_model.CreacionProductoViewModel
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import com.android_abel.indah._view_ui.base.EscanerListener
import com.android_abel.indah._view_ui.base.FileListener
import com.android_abel.indah.utils.CustomsConstantes
import com.android_abel.indah.utils.base.Base64
import com.android_abel.indah.utils.extensiones.functionWithParams
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_creacion_producto.*


class CreacionProductoFragment : BaseFragment(), BasicMethods, EscanerListener, FileListener {

    private var productoEntity: ProductoEntity? = null
    private var urlImage: String? = null

    val creacionProductoViewModel by lazy {
        ViewModelProviders.of(this).get(CreacionProductoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_creacion_producto, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArgument()
        initObservables()
        init()
        initListeners()
    }

    private fun initArgument() {
        val argumentProducto = arguments?.getSerializable(CustomsConstantes.EXTRAS_VIEW_PRODUCT)
        if (argumentProducto != null) {
            productoEntity = argumentProducto as ProductoEntity
            try {
                cargarDatosAVista()
                functionWithParams(1000) {
                    showToast(it)
                }
            } catch (e: Exception) {
                showToast(getString(R.string.error_cargar_vista_con_producto))
            }
        }
    }

    override fun initObservables() {

    }

    override fun init() {
    }

    override fun initListeners() {

        textViewGuardarProducto.setOnClickListener {
            if (validate()) {
                val produtoNuevo = generateProducto()

                if (productoEntity != null) {
                    produtoNuevo.id = productoEntity!!.id
                    creacionProductoViewModel.update(produtoNuevo)
                } else {
                    creacionProductoViewModel.insert(produtoNuevo)
                }
                clearForm()
            } else
                Toast.makeText(context, getString(R.string.error_insertar_producto), Toast.LENGTH_LONG).show()

        }

        floatingActionButtonScanner_creationProducto.setOnClickListener {
            openScaner(this)
        }

        imageViewSelectPhoto.setOnClickListener {
            SelectImage(this)
        }
    }

    private fun validate(): Boolean {
        return (editTextNombreProducto_creacion.text?.isNotEmpty() ?: false
                && editTextCodigoProducto_creacion.text?.isNotEmpty() ?: false
                && editTextCantidad_creacion.text?.isNotEmpty() ?: false
                && editTextCantidadReserva_creacion.text?.isNotEmpty() ?: false
                && editTextPrecioCompra_creacion.text?.isNotEmpty() ?: false
                && editTextPrecioVenta_creacion.text?.isNotEmpty() ?: false
                && editTextDescripcion_creacion.text?.isNotEmpty() ?: false)
    }

    private fun generateProducto(): ProductoEntity {
        val producto = ProductoEntity()
        producto.urlImagen = urlImage
        producto.nombre = editTextNombreProducto_creacion.text.toString()
        producto.codigo = editTextCodigoProducto_creacion.text.toString()
        producto.cantidad = editTextCantidad_creacion.text.toString().toInt()
        producto.cantidadReserva = editTextCantidadReserva_creacion.text.toString().toInt()
        producto.precioCompra = editTextPrecioCompra_creacion.text.toString().toInt()
        producto.precioVenta = editTextPrecioVenta_creacion.text.toString().toInt()
        producto.descripcion = editTextDescripcion_creacion.text.toString()

        return producto
    }

    private fun clearForm() {
        editTextNombreProducto_creacion.setText("")
        editTextCodigoProducto_creacion.setText("")
        editTextCantidad_creacion.setText("")
        editTextCantidadReserva_creacion.setText("")
        editTextPrecioCompra_creacion.setText("")
        editTextPrecioVenta_creacion.setText("")
        editTextDescripcion_creacion.setText("")
    }

    override fun codeFromScanner(code: String) {
        editTextCodigoProducto_creacion.setText(code)
    }

    override fun codeNoFound() {

    }

    private fun cargarDatosAVista() {
        editTextNombreProducto_creacion.setText(productoEntity?.nombre ?: "")
        editTextCodigoProducto_creacion.setText(productoEntity?.codigo ?: "")
        editTextCantidad_creacion.setText(productoEntity?.cantidad.toString())
        editTextCantidadReserva_creacion.setText(productoEntity?.cantidadReserva.toString())
        editTextPrecioCompra_creacion.setText(productoEntity?.precioCompra.toString())
        editTextPrecioVenta_creacion.setText(productoEntity?.precioVenta.toString())
        editTextDescripcion_creacion.setText(productoEntity?.descripcion ?: "")

        Glide.with(requireContext())
            .load(productoEntity?.urlImagen)
            .placeholder(R.drawable.ic_scan)
            .error(R.drawable.ic_scan)
            .centerCrop()
            .into(imageViewImageProducto)
    }

    override fun imageUrlSelectedFromGallery(url: String) {
        urlImage = url
        Glide.with(requireContext())
            .load(productoEntity?.urlImagen)
            .placeholder(R.drawable.ic_scan)
            .error(R.drawable.ic_scan)
            .centerCrop()
            .into(imageViewImageProducto)
    }

    override fun imageUriSelectedFromGallery(url: Bitmap) {
        imageViewImageProducto.setImageBitmap(url)
        creacionProductoViewModel.uploadImage(url)
    }


}
