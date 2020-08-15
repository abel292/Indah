package com.android_abel.indah._view_ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.android_abel.indah.R
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import com.android_abel.indah._view_ui.fragments.ventas.VentasFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            VentasFragment()
    }

    lateinit var homeView: View
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeView = inflater.inflate(R.layout.fragment_home, container, false)

        return homeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservables()
        init()
        initListeners()
    }

    override fun initObservables() {
    }

    override fun init() {

    }

    override fun initListeners() {

    }

    fun goToProductos() {
        buttonIrProductos.findNavController().navigate(R.id.action_homeFragment_to_productosFragment)
    }

    fun goVentas() {
        buttonIrVentas.findNavController().navigate(R.id.action_homeFragment_to_ventasFragment)
    }

    fun goToGestion() {
        buttonIrGestion.findNavController().navigate(R.id.action_homeFragment_to_gestionFragment)
    }

}
