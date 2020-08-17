package com.android_abel.indah._view_ui.activities

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import com.android_abel.indah.R
import com.android_abel.indah._view_ui.base.BaseActivity
import com.android_abel.indah.utils.CustomsConstantes
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class EscanerActivity : BaseActivity(), ZXingScannerView.ResultHandler {

    var soundBeep: MediaPlayer? = null
    private var mScannerView: ZXingScannerView? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        initObservables()
        init()
        initListeners()
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun initObservables() {
    }

    override fun init() {
        checkPermisssions()
        mScannerView = ZXingScannerView(baseContext)
        setContentView(mScannerView)
        mScannerView!!.setAutoFocus(true)
        soundBeep = MediaPlayer.create(baseContext, R.raw.beep)
    }

    override fun initListeners() {
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        soundBeep?.start()
        val intent = Intent()
        intent.putExtra(CustomsConstantes.EXTRAS_CODIGO_ESCANEO, rawResult.text)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
