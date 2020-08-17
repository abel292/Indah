package com.android_abel.indah._view_ui.base

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android_abel.indah.R
import com.android_abel.indah._view_ui.activities.EscanerActivity
import com.android_abel.indah.utils.CustomsConstantes
import com.google.android.material.snackbar.Snackbar
import com.phelat.navigationresult.BuildConfig

abstract class BaseActivity : AppCompatActivity(), BasicMethods {

    protected val TAG = this.javaClass.simpleName
    private var currentFragment: Fragment? = null
    var progressBarDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OPEN ACTIVITY $TAG")
        super.onCreate(savedInstanceState)
    }

    protected fun replaceFragment(fragment: Fragment, layoutContainer: Int, addStack: Boolean) {
        if (fragment.isVisible) {
            if (BuildConfig.DEBUG) Log.d(TAG, "El fragmento ya se encuentra visible")
            return
        }
        val obj = supportFragmentManager
            .beginTransaction()
            .replace(layoutContainer, fragment)
        if (addStack) obj.addToBackStack(fragment.javaClass.simpleName)
        obj.commit()
        currentFragment = fragment
    }

    fun changeStatusBarColor(color: String?) {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(color)
        } else {
            Log.e(
                "hexadecimal4",
                Build.VERSION.SDK_INT.toString() + " es menor a la api 21"
            )
        }
    }

    fun showSnackBar(view: View?, msg: String?) {
        val snack = Snackbar.make(view!!, msg!!, Snackbar.LENGTH_LONG)
        val viewSnack = snack.view
        val tv = viewSnack.findViewById<View>(R.id.snackbar_text) as TextView
        tv.setTextColor(Color.WHITE)
        snack.show()
    }

    fun showDialogError(code: Int, msg: String?, title: String) {
        val alertDialogBuilder =
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(
                    "OK"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        if (BuildConfig.DEBUG && msg != null && !msg.isEmpty()) {
            alertDialogBuilder.setTitle("Dev Error")
                .setMessage("code: $code $msg")
        }
        alertDialogBuilder.create().show()
    }

    fun showDialogMessageError(title: String?, msg: String?) {
        val alertDialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                )
        alertDialogBuilder.create().show()
    }

    protected fun showDialogMessage(title: String?, msg: String?) {
        val alertDialogBuilder =
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(
                    "OK"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        alertDialogBuilder.create().show()
    }

    fun checkPermisssions() {
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA), CustomsConstantes.MY_PERMISSIONS_REQUEST_CAMERA
                )
            }
        }
    }

    fun hideSystemUI(mDecorView: View) { // Set the IMMERSIVE flag.

        mDecorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    fun showSystemUI(mDecorView: View) {
        mDecorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    fun hideNavigationBar() {
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}