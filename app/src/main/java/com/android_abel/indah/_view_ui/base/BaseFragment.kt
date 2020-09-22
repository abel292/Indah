package com.android_abel.indah._view_ui.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.android_abel.indah.R
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.venta.VentaEntity
import com.android_abel.indah._view_ui.activities.EscanerActivity
import com.android_abel.indah._view_ui.activities.HomeActivity
import com.android_abel.indah._view_ui.fragments.productos.ProductosFragment
import com.android_abel.indah.utils.CustomsConstantes
import com.android_abel.indah.utils.CustomsConstantes.Companion.EXTRAS_CODIGO_ESCANEO
import com.android_abel.indah.utils.CustomsConstantes.Companion.EXTRAS_SELECT_IMGAE
import com.android_abel.indah.utils.CustomsConstantes.Companion.REQUEST_CODE_SCANNER
import com.phelat.navigationresult.BundleFragment
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


abstract class BaseFragment : BundleFragment(), BasicMethods {

    val TAG = this.javaClass.simpleName
    protected var mActivity: BaseActivity? = null

    //views
    protected lateinit var fragmentView: View

    lateinit var escanerListener: EscanerListener
    var fileListener: FileListener? = null

    private var filePath: Uri? = null


    companion object {
        const val REQUEST_CODE_COORD_MAP = 1000
        const val REQUEST_CODE_FINISH_CREATION_PROJECT = 1234

        // request code
        private const val PICK_IMAGE_REQUEST = 22


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OPEN FRAGMENT $TAG")
        super.onCreate(savedInstanceState)
        mActivity = activity as BaseActivity?
        configStatusBar()
    }


    protected fun hideKeyBoard() {

        if (activity == null) return
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

    fun desactivarBackPressed() {
        mActivity?.backPressedEnable = false
    }

    fun activarBackPressed() {
        mActivity?.backPressedEnable = true
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

    fun pickUserImage(fileListener: FileListener) {
        this.fileListener = fileListener
        val photoPickerIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        photoPickerIntent.type = "image/, video/*"
        photoPickerIntent.putExtra("crop", "true")
        photoPickerIntent.putExtra("scale", true)
        photoPickerIntent.putExtra("outputX", 256)
        photoPickerIntent.putExtra("outputY", 256)
        photoPickerIntent.putExtra("aspectX", 1)
        photoPickerIntent.putExtra("aspectY", 1)
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri())
        startActivityForResult(photoPickerIntent, EXTRAS_SELECT_IMGAE)

    }

    private fun getTempUri(): Uri? {
        return Uri.fromFile(getTempFile())
    }

    private fun getTempFile(): File? {
        return if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            val file = File(Environment.getExternalStorageDirectory(), getString(R.string.temp_url))
            try {
                file.createNewFile()
            } catch (e: IOException) {
            }
            file
        } else {
            null
        }
    }

    // Select Image method
    fun SelectImage(fileListener: FileListener) {

        this.fileListener = fileListener
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(i, PICK_IMAGE_REQUEST)
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
        if (requestCode == EXTRAS_SELECT_IMGAE) {
            if (data != null) {
                val uri = data.data
                fileListener?.imageUrlSelectedFromGallery(uri.toString())
                showToast(uri.toString())
            }

        }

        if (requestCode == PICK_IMAGE_REQUEST) {
            val selectedImage = data!!.data
            if (selectedImage != null) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, selectedImage))
                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImage)
                }
                fileListener?.imageUriSelectedFromGallery(bitmap)
            }

        }

    }


}