package com.android_abel.indah._view_ui.fragments.gestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.android_abel.indah.R
import com.android_abel.indah._view_ui.base.BaseFragment
import com.android_abel.indah._view_ui.base.BasicMethods
import kotlinx.android.synthetic.main.fragment_gestion.*

class GestionFragment : BaseFragment() {
    companion object {

        @JvmStatic
        fun newInstance() =
            GestionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_gestion, container, false)
        return fragmentView
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
        textViewHistorialVentas.setOnClickListener {
            it.goTo(R.id.action_gestionFragment_to_historialVentasFragment)
        }

        textViewVentasPorCobrar.setOnClickListener {
            it.goTo(R.id.action_gestionFragment_to_ventasPorCobrarFragment)
        }

        textViewClientes.setOnClickListener {
            it.goTo(R.id.action_gestionFragment_to_clientesFragment)
        }


    }


}
