package com.zsk.androtweet.utils.databinding

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.kaloglu.library.databinding4vm.adapter.DataBoundPagedListAdapter
import com.kaloglu.library.ktx.isNotNullOrEmpty
import com.kaloglu.library.ktx.withAnimation
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

@BindingAdapter("isDeleted", "result")
fun setErrorLineBackground(
        view: MaterialTextView,
        isDeleted: Boolean,
        result: String
) {

    view.withAnimation(isDeleted || result.isNotNullOrEmpty(), alpha = 1F)
    when {
        isDeleted -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.deletedTweet))
        result.isNotNullOrEmpty() -> {
            view.text = result
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.failedTweet))
        }
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