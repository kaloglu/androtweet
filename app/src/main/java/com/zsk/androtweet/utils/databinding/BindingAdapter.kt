package com.zsk.androtweet.utils.databinding

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.kaloglu.library.databinding4vm.adapter.DataBoundListAdapter
import com.kaloglu.library.databinding4vm.adapter.DataBoundPagedListAdapter
import com.kaloglu.library.ktx.isNotNullOrEmpty
import com.kaloglu.library.ktx.withAnimation
import com.kaloglu.library.ui.RecyclerItem
import com.zsk.androtweet.R
import com.zsk.androtweet.viewmodels.PagingManager

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

@BindingAdapter("adapter", "items", requireAll = true)
fun <RI : RecyclerItem> setItems(
        recyclerView: RecyclerView,
        adapter: DataBoundListAdapter<RI>,
        items: List<RI>
) {
    recyclerView.adapter = adapter.apply {
        submitList(items)
    }

}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("adapter", "items", "pagingManager", requireAll = true)
fun <RI : RecyclerItem> setPagedItems(
        recyclerView: RecyclerView,
        adapter: DataBoundPagedListAdapter<RI>,
        items: List<RI>,
        pagingManager: PagingManager<*, RI>
) {
    recyclerView.adapter = adapter.apply {
        submitList(pagingManager.getPagedList(items))
    }

}
