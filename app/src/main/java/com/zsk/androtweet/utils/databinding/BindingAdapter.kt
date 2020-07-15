package com.zsk.androtweet.utils.databinding

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.kaloglu.library.databinding4vm.adapter.DataBoundPagedListAdapter
import com.kaloglu.library.ui.RecyclerItem
import com.zsk.androtweet.R

@BindingAdapter("tweetSelection")
fun setBackground(
        view: View,
        isSelected: Boolean
) {
    when (isSelected) {
        true -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.selectedTweet))
        else -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
    }

}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("pagedItems")
fun <RI : RecyclerItem> setPagedItems(
        recyclerView: RecyclerView,
        items: PagedList<RI>
) {
    recyclerView.adapter?.apply {
        (this as DataBoundPagedListAdapter<RI>)
        submitList(items)
    }

}