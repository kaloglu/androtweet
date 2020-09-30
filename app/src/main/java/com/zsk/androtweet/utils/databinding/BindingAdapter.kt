package com.zsk.androtweet.utils.databinding

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView
import com.kaloglu.library.ktx.empty
import com.kaloglu.library.ktx.isNotNullOrEmpty
import com.kaloglu.library.ktx.withAnimation
import com.zsk.androtweet.R

@BindingAdapter("tweetSelection")
fun setBackground(
        view: View,
        isSelected: Boolean
) {
    view.withAnimation(isSelected, alpha = 1F)
    when (isSelected) {
        true -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.selectedTweet))
        else -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
    }

}

@BindingAdapter("isDeleted", "result", requireAll = false)
fun setErrorLineBackground(
        view: MaterialTextView,
        isDeleted: Boolean,
        result: String? = String.empty
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
