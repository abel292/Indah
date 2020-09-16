package com.android_abel.indah._view_ui.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah._model.local.cliente.ClienteEntity

abstract class HolderBaseTwo<T>(
    inflater: LayoutInflater,
    parent: ViewGroup,
    viewInflater: Int
) :
    HolderBase<T>(inflater,parent,viewInflater) {

    abstract fun bind(objeto: T, objeto2: ClienteEntity, position: Int)
}