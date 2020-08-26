package com.android_abel.indah._view_ui.adapters.ventas

import android.view.View

interface DraggableViewListener {
    fun onMoveDragger(v: View)
    fun onDropRitgh()
    fun onDropLeft()
    fun positionInitial(v: View)
}