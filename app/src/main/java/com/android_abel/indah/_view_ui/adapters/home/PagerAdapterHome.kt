package com.android_abel.indah._view_ui.adapters.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android_abel.indah._view_ui.fragments.gestion.GestionFragment
import com.android_abel.indah._view_ui.fragments.home.HomeFragment
import com.android_abel.indah._view_ui.fragments.productos.ProductosFragment
import com.android_abel.indah._view_ui.fragments.ventas.VentasFragment

class PagerAdapterHome(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    // Returns total number of pages
    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 // Fragment # 0 - This will show FirstFragment
            -> {
                ProductosFragment.newInstance()
            }
            1 // Fragment # 1 - This will show SecondFragment
            -> {
                VentasFragment.newInstance()
            }
            2 // Fragment # 2 - This will show ThirdFragment
            -> {
                GestionFragment.newInstance()
            }// Fragment # 3 - This will show FourthFragment
            else -> ProductosFragment.newInstance()
        }
    }

    companion object {
        private const val NUM_ITEMS = 4
    }

}
