package com.android_abel.indah._view_ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.android_abel.indah.R
import com.phelat.navigationresult.BundleFragment

abstract class BaseFragment : BundleFragment(), BasicMethods {

    val TAG = this.javaClass.simpleName
    protected var activity: BaseActivity? = null

    //views
    protected lateinit var fragmentView: View


    companion object {
        const val REQUEST_CODE_COORD_MAP = 1000
        const val REQUEST_CODE_FINISH_CREATION_PROJECT = 1234

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OPEN FRAGMENT $TAG")
        super.onCreate(savedInstanceState)
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

    fun onBackPressed() {
        requireActivity().onBackPressed()
    }
}