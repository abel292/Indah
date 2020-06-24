package com.android_abel.indah._view_ui.fragments.ventasPorCobrar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android_abel.indah.R
import com.android_abel.indah._view_ui.base.BaseFragment

class VentasPorCobrarFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView= inflater.inflate(R.layout.fragment_ventas_por_cobrar, container, false)
        return fragmentView
    }

    override fun initObservables() {
    }

    override fun init() {
    }

    override fun initListeners() {
    }

}
