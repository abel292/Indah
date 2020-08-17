package com.android_abel.indah._view_ui.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_ui.activities.EscanerActivity
import com.android_abel.indah._view_ui.activities.HomeActivity
import com.android_abel.indah._view_ui.fragments.productos.ProductosFragment
import com.android_abel.indah.utils.CustomsConstantes
import com.android_abel.indah.utils.CustomsConstantes.Companion.EXTRAS_CODIGO_ESCANEO
import com.android_abel.indah.utils.CustomsConstantes.Companion.REQUEST_CODE_SCANNER
import com.phelat.navigationresult.BundleFragment


abstract class BaseFragment : BundleFragment(), BasicMethods {

    val TAG = this.javaClass.simpleName
    protected var mActivity: BaseActivity? = null

    //views
    protected lateinit var fragmentView: View

    lateinit var escanerListener: EscanerListener


    companion object {
        const val REQUEST_CODE_COORD_MAP = 1000
        const val REQUEST_CODE_FINISH_CREATION_PROJECT = 1234

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OPEN FRAGMENT $TAG")
        super.onCreate(savedInstanceState)
        mActivity = activity as BaseActivity?
        configStatusBar()
    }


    protected fun hideKeyBoard() {

        if (getActivity() == null) return
        val view = requireActivity().currentFocus
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    protected fun showDialogMessage(title: String?, msg: String?) {

        val alertDialogBuilder =
            AlertDialog.Builder(requireActivity())
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(
                    "OK"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        alertDialogBuilder.create().show()
    }

    fun View.goTo(action: Int) {
        findNavController().navigate(action)

    }

    fun View.goToWithProducto(action: Int, productoEntity: ProductoEntity) {
        val bundle = Bundle()
        bundle.putSerializable(CustomsConstantes.EXTRAS_VIEW_PRODUCT, productoEntity)
        findNavController().navigate(action, bundle)
    }

    fun View.goToWithVenta(action: Int, ventaEntity: VentaEntity) {
        val bundle = Bundle()
        bundle.putSerializable(CustomsConstantes.EXTRAS_VIEW_VENTA, ventaEntity)
        findNavController().navigate(action, bundle)
    }

    fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    fun onBackPressed() {
        val currentFragment = this
        val homeActivity = requireActivity() as HomeActivity
        if (currentFragment is ProductosFragment) {
            homeActivity.onBackPressed()
        } else {
            mActivity?.onBackPressed()
        }


    }

    private fun configStatusBar() {
        mActivity?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = Color.TRANSPARENT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            }
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        }
    }

    fun EditText.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

    fun openScaner(escanerListener: EscanerListener) {
        this.escanerListener = escanerListener
        val intentScanear = Intent(context, EscanerActivity::class.java)
        startActivityForResult(intentScanear, REQUEST_CODE_SCANNER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCANNER) {
            if (data != null) {
                val codigo = data.getStringExtra(EXTRAS_CODIGO_ESCANEO)
                escanerListener.codeFromScanner(codigo ?: "")
            } else {
                escanerListener.codeNoFound()
            }
        }

    }
}