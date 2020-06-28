package com.zsk.androtweet.utils.databinding

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
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
