package com.android_abel.indah._view_ui.adapters.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._view_ui.adapters.historialVentas.AdapterHistorial

abstract class BaseAdapterRecycler<T>() : RecyclerView.Adapter<HolderBase<T>>() {
}