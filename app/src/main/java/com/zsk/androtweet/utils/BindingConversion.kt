package com.zsk.androtweet.utils

import android.view.View
import androidx.databinding.BindingConversion

@BindingConversion
fun booleanToVisibilty(isVisible: Boolean) =
        when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }

@BindingConversion
fun convertIntToString(value: Int?) = value.toString()