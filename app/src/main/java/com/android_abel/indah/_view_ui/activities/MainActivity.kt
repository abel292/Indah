package com.android_abel.indah._view_ui.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.android_abel.indah.R
import com.android_abel.indah._view_ui.adapters.home.PagerAdapterHome
import com.android_abel.indah._view_ui.base.BaseActivity
import com.android_abel.indah._view_ui.fragments.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), View.OnTouchListener {

    private val pagerAdapter by lazy {
        PagerAdapterHome(supportFragmentManager)
    }

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
        setContentView(R.layout.activity_main)

        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
    }

    override fun init() {

    }

    override fun initListeners() {
        motion_layout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (currentAnim == TAB_REARRANGEMENT_ANIM && motion_layout.progress == 1f && transitionTabIndex != 0) {
                    currentAnim = TAB_SLIDE_ANIM
                }
            }
        })

        btn_back.setOnClickListener {
            if (currentAnim == TAB_SLIDE_ANIM) {
                currentAnim = TAB_REARRANGEMENT_ANIM
                motion_layout.setTransition(R.id.start, lastEndTransition)
                motion_layout.progress = 1f
                motion_layout.setTransitionDuration(1000)
                motion_layout.transitionToStart()
                animProgress = 0f
            }
        }

        first_image.setOnClickListener {
            if (currentAnim == TAB_REARRANGEMENT_ANIM) {
                lastEndTransition = R.id.endFirst
                motion_layout.setTransition(R.id.start, R.id.endFirst)
                motion_layout.setTransitionDuration(1000)
                motion_layout.transitionToEnd()
                setTabIndicator()
                transitionTabIndex = 0
                currentAnim = TAB_SLIDE_ANIM


                it.findNavController()
            }
        }

        second_image.setOnClickListener {
            if (currentAnim == TAB_REARRANGEMENT_ANIM) {
                lastEndTransition = R.id.endSecond
                motion_layout.setTransition(R.id.start, R.id.endSecond)
                motion_layout.setTransitionDuration(1000)
                motion_layout.transitionToEnd()
                transitionTabIndex = 1
                currentAnim = TAB_SLIDE_ANIM
                setTabIndicator()
            }
        }

        third_image.setOnClickListener {
            if (currentAnim == TAB_REARRANGEMENT_ANIM) {
                transitionTabIndex = 2
                lastEndTransition = R.id.endThird
                motion_layout.setTransition(R.id.start, R.id.endThird)
                motion_layout.setTransitionDuration(1000)
                motion_layout.transitionToEnd()
                currentAnim = TAB_SLIDE_ANIM
                setTabIndicator()
            }
        }

        fourth_image.setOnClickListener {
            if (currentAnim == TAB_REARRANGEMENT_ANIM) {

                transitionTabIndex = 3
                //no hace nada por que esta oculto

                lastEndTransition = R.id.endFourth
                motion_layout.setTransition(R.id.start, R.id.endFourth)
                motion_layout.setTransitionDuration(1000)
                motion_layout.transitionToEnd()

                currentAnim = TAB_SLIDE_ANIM
                setTabIndicator()
            }
        }

        touch_view.setOnTouchListener(this) // Swipe Area for controlling touch movement
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val action: Int = event.action

        if (currentAnim == TAB_SLIDE_ANIM) {
            currentAnim = TAB_REARRANGEMENT_ANIM

            motion_layout.setTransition(R.id.start, lastEndTransition)
            motion_layout.progress = 1f
            animProgress = 1f
        }

        return when (action) {
            MotionEvent.ACTION_MOVE -> {
                animProgress += (oldY - event.rawY) * 0.0015f // Implementing swipe up and down behavior

                // Clamp to maximum animation value
                if (animProgress > 1f) {
                    animProgress = 1f
                }

                // Clamp to minimum animation value
                if (animProgress < 0) {
                    animProgress = 0f
                }
                motion_layout.progress = animProgress
                oldY = event.rawY.toInt()

                true
            }
            MotionEvent.ACTION_UP -> {
                oldY = event.rawY.toInt()
                returnToRelativePosition()
                true
            }
            else -> {
                oldY = event.rawY.toInt()
                true
            }
        }
    }

    override fun onBackPressed() {
        if (currentAnim == TAB_SLIDE_ANIM) {
            currentAnim = TAB_REARRANGEMENT_ANIM

            motion_layout.setTransition(R.id.start, lastEndTransition)
            motion_layout.progress = 1f
            motion_layout.setTransitionDuration(1000)
            motion_layout.transitionToStart()

            animProgress = 0f
        }
    }

    private fun setTabIndicator() {
        when (transitionTabIndex) {
            0 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                fourth_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
            }
            1 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                fourth_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
            }
            2 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
                fourth_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
            }
            3 -> {
                first_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                second_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                third_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.inActive))
                fourth_indicator.setBackgroundColor(ContextCompat.getColor(this, R.color.active))
            }
        }
    }

    private fun returnToRelativePosition() {
        if (animProgress <= 0f || animProgress >= 1f) {
            return
        }
        if (animProgress < 0.25) {
            animProgress = 0f
            motion_layout.transitionToStart()
            currentAnim = TAB_REARRANGEMENT_ANIM
        } else {
            animProgress = 1f
            motion_layout.transitionToEnd()
            currentAnim = TAB_SLIDE_ANIM
        }
    }
}
