package com.zsk.androtweet.utils

import android.view.View
import androidx.databinding.BindingConversion
import com.kaloglu.library.ktx.GenericExtensions
import com.kaloglu.library.ktx.toDate
import com.kaloglu.library.ktx.toDateString
import java.util.*

object BindingConversions {
    @BindingConversion
    @JvmStatic
    fun booleanToVisibilty(isVisible: Boolean) =
            when (isVisible) {
                true -> View.VISIBLE
                else -> View.GONE
            }

    @BindingConversion
    @JvmStatic
    fun intToString(value: Int) = value.toString()

    @BindingConversion
    @JvmStatic
    fun stringToDate(value: String) = value.toDate(GenericExtensions.UIDateStringPattern)

    @BindingConversion
    @JvmStatic
    fun timestampToDate(value: Long) = value.toDate()

    @BindingConversion
    @JvmStatic
    fun dateToString(date: Date): String = date.toDateString(GenericExtensions.UIDateStringPattern)
}