package com.android_abel.indah._view_ui.adapters.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android_abel.indah.R

abstract class HolderBase<T>(
    inflater: LayoutInflater,
    parent: ViewGroup,
    viewInflater: Int
) :
    RecyclerView.ViewHolder(
        inflater.inflate(
            viewInflater,
            parent,
            false
        )
    ) {

    abstract fun bind(objeto: T, position: Int)
}