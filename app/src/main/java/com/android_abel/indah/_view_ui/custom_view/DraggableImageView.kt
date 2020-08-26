package com.android_abel.indah._view_ui.custom_view

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.android_abel.indah._view_ui.adapters.ventas.DraggableViewListener
import com.android_abel.indah._view_ui.custom_view.Draggable.DRAG_TOLERANCE
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class DraggableImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs) {
    private var draggableListener: DraggableViewListener? = null
    private var widgetInitialX: Float = 0F
    private var widgetDX: Float = 0F
    private var widgetInitialY: Float = 0F
    private var widgetDY: Float = 0F

    private var activity: Activity? = null

    init {
        draggableSetup()
    }

    private fun draggableSetup() {

        this.setOnTouchListener { v, event ->
            val viewParent = v.parent as View
            val parentHeight = viewParent.height
            val parentWidth = viewParent.width
            val xMax = parentWidth - v.width
            val xMiddle = parentWidth / 2
            val yMax = parentHeight - v.height

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    widgetDX = v.x - event.rawX
                    widgetDY = v.y - event.rawY
                    widgetInitialX = v.x
                    widgetInitialY = v.y

                }
                MotionEvent.ACTION_MOVE -> {
                    var newX = event.rawX + widgetDX
                    newX = max(0F, newX)
                    newX = min(xMax.toFloat(), newX)
                    v.x = newX

                    var newY = event.rawY + widgetDY
                    newY = max(0F, newY)
                    newY = min(yMax.toFloat(), newY)
                    v.y = newY

                    draggableListener?.onMoveDragger(v)

                }
                MotionEvent.ACTION_UP -> {

                    if (event.rawX >= xMiddle) {
                        draggableListener?.onDropRitgh()
                    } else {
                        draggableListener?.onDropLeft()
                    }

                    goToPositionInitial()
                    if (abs(v.x - widgetInitialX) <= DRAG_TOLERANCE && abs(v.y - widgetInitialY) <= DRAG_TOLERANCE) {
                        performClick()
                    } //else draggableListener?.xAxisChanged(event.rawX >= xMiddle)


                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(200)
                }
                else -> return@setOnTouchListener false
            }
            true
        }
    }

    override fun performClick(): Boolean {
        Log.e("DraggableImageView", "$x , $y ")
        goToPositionInitial()
        return super.performClick()
    }

    fun goToPositionInitial() {
        val size = Point()
        activity?.windowManager?.defaultDisplay?.getSize(size)
        val width: Int = size.x
        val height: Int = size.y

        val positionX = (width - 200).toFloat()
        val positionY = (height - 200).toFloat()

        Log.e("DraggableImageView", "$positionX , $positionY ")

        animate().x(positionX)
            .y(positionY)
            .setDuration(Draggable.DURATION_MILLIS)
            .setUpdateListener { draggableListener?.positionInitial(this) }
            .start()
    }

    fun setListener(activity: Activity?, draggableListener: DraggableViewListener?) {
        this.draggableListener = draggableListener
        this.activity = activity
    }
}

object Draggable {
    const val DRAG_TOLERANCE = 26
    const val DURATION_MILLIS = 250L
}
