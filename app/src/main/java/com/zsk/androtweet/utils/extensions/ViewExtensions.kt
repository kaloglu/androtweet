package com.zsk.androtweet.utils.extensions

import android.view.View
import com.kaloglu.library.databinding4vm.adapter.DataBoundRecyclerAdapter
import com.kaloglu.library.ui.RecyclerItem

fun <A : DataBoundRecyclerAdapter<RI>, RI : RecyclerItem>
        A.setItemClickListener(onClick: ((RI, Int) -> Unit)): A {
    this.onItemClick = onClick
    return this
}

fun <A : DataBoundRecyclerAdapter<RI>, RI : RecyclerItem>
        A.setViewClickListener(onClick: ((RI, View, Int) -> Unit)): A {
    this.onViewClick = onClick
    return this
}
