package com.android_abel.indah._view_ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.android_abel.indah.R
import com.android_abel.indah._view_ui.base.BaseActivity
import com.android_abel.indah._view_ui.fragments.gestion.GestionFragment
import com.android_abel.indah._view_ui.fragments.productos.ProductosFragment
import com.android_abel.indah._view_ui.fragments.ventas.VentasFragment
import com.android_abel.indah.utils.CustomsConstantes
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    lateinit var navController: NavController

    companion object {
        const val TAB_REARRANGEMENT_ANIM = 0
        const val TAB_SLIDE_ANIM = 1
    }

    private var transitionTabIndex = 0
    private var currentAnim = TAB_REARRANGEMENT_ANIM
    private var lastEndTransition = R.id.endFirst
    private var animProgress: Float = 0f
    private var oldY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
    }

    override fun init() {

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

    }

    override fun initListeners() {

        btn_back.setOnClickListener {
            if (currentAnim == TAB_SLIDE_ANIM) {
                currentAnim = TAB_REARRANGEMENT_ANIM
                motion_layout.setTransition(R.id.start, lastEndTransition)
                motion_layout.progress = 1f
                motion_layout.setTransitionDuration(700)
                motion_layout.transitionToStart()
                animProgress = 0f

            }
        }

        first_image.setOnClickListener {
            navController.navigate(R.id.productosFragment)
            if (currentAnim == TAB_REARRANGEMENT_ANIM) {
                transitionTabIndex = 0
                lastEndTransition = R.id.endFirst
                motion_layout.setTransition(R.id.start, R.id.endFirst)
                motion_layout.setTransitionDuration(700)
                motion_layout.transitionToEnd()
                currentAnim = TAB_SLIDE_ANIM
                setTabIndicator()
            }
        }

        second_image.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(CustomsConstantes.EXTRAS_VENTAS_MODO_INICIO, false)
            navController.navigate(R.id.ventasFragment, bundle)

            if (currentAnim == TAB_REARRANGEMENT_ANIM) {
                transitionTabIndex = 1
                lastEndTransition = R.id.endSecond
                motion_layout.setTransition(R.id.start, R.id.endSecond)
                motion_layout.setTransitionDuration(700)
                motion_layout.transitionToEnd()
                currentAnim = TAB_SLIDE_ANIM
                setTabIndicator()
            }
        }

        third_image.setOnClickListener {
            navController.navigate(R.id.gestionFragment)
            if (currentAnim == TAB_REARRANGEMENT_ANIM) {
                transitionTabIndex = 2
                lastEndTransition = R.id.endThird
                motion_layout.setTransition(R.id.start, R.id.endThird)
                motion_layout.setTransitionDuration(700)
                motion_layout.transitionToEnd()
                currentAnim = TAB_SLIDE_ANIM
                setTabIndicator()

            }
        }
    }

    override fun onBackPressed() {

        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val current = navHostFragment?.childFragmentManager?.fragments?.get(0)

        if (current is ProductosFragment || current is VentasFragment || current is GestionFragment) {
            if (currentAnim == TAB_SLIDE_ANIM) {
                currentAnim = TAB_REARRANGEMENT_ANIM
                motion_layout.setTransition(R.id.start, lastEndTransition)
                motion_layout.progress = 1f
                motion_layout.setTransitionDuration(700)
                motion_layout.transitionToStart()
                motion_layout.setBackgroundColor(Color.BLACK)
                animProgress = 0f
            }
        } else {
            super.onBackPressed()
        }

    }

    private fun setTabIndicator() {
        when (transitionTabIndex) {
            0 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
            }
            1 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
            }
            2 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
            }
            3 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
            }
        }
    }

}
