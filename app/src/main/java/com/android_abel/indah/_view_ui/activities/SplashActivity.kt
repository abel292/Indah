package com.android_abel.indah._view_ui.activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.android_abel.indah.R
import com.android_abel.indah._view_ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    private var fileName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
    }

    override fun init() {

        try {
            configStatusBar()
            mVideoView.setOnPreparedListener { mediaPlayer ->
                val videoRatio = mediaPlayer.videoWidth / mediaPlayer.videoHeight.toFloat()
                val screenRatio = mVideoView.width / mVideoView.height.toFloat()
                val scaleX = videoRatio / screenRatio
                if (scaleX >= 1f) {
                    mVideoView.scaleX = scaleX
                } else {
                    mVideoView.scaleY = 1f / scaleX
                }
            }

            fileName = "android.resource://$packageName/raw/video"
            mVideoView.setVideoURI(Uri.parse(fileName))
            mVideoView.start()
            mVideoView.requestFocus()
            hideSystemUI()

            mVideoView.setOnCompletionListener {
                goToHome()
            }
        } catch (e: Exception) {
            goToHome()
        }

    }

    override fun initListeners() {
    }

    private fun configStatusBar() {
        window.apply {
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

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT > 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}