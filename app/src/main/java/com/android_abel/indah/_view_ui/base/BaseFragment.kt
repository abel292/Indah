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
import com.phelat.navigationresult.BundleFragment

abstract class BaseFragment : BundleFragment() {

    protected val TAG = this.javaClass.simpleName
    protected var activity: BaseActivity? = null

    companion object {
        const val REQUEST_CODE_COORD_MAP = 1000
        const val REQUEST_CODE_FINISH_CREATION_PROJECT = 1234

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OPEN FRAGMENT $TAG")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        requireView().setOnClickListener { v: View? ->
            Log.d(
                TAG,
                "click $TAG"
            )
        }

        return view
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

    /*protected fun showCustomDialogMessage() {
        val mDialogView = LayoutInflater.from(getActivity()!!).inflate(R.layout.dialog_confirm_approval, null)

        val alertDialogBuilder =
            AlertDialog.Builder(getActivity()!!)
                .setView(mDialogView)
                *//*.setPositiveButton(
                    resources.getString(R.string.ok)
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }*//*
        val mAlertDialog = alertDialogBuilder.show()

        mDialogView.dialogConfirmApproval_button_accept.setOnClickListener {

        }
        mDialogView.dialogConfirmApproval_button_cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }*/

    fun onBackPressed() {
        requireActivity().onBackPressed()
    }
}