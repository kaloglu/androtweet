package com.zsk.androtweet.utils.databinding

import android.view.View
import androidx.databinding.BindingConversion
import com.kaloglu.library.ktx.*
import com.zsk.androtweet.utils.Constants
import java.util.*
import java.util.concurrent.TimeUnit

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
    fun stringToDate(value: String) = value.toDate(Constants.UI_DATE_PATTERN)

    @BindingConversion
    @JvmStatic
    fun timestampToDate(value: Long) = value.toDate()

    @BindingConversion
    @JvmStatic
    fun dateToString(date: Date): String {
        val difs = currentTimestamp() - date.time
        val difdays = TimeUnit.DAYS.convert(difs, TimeUnit.MILLISECONDS)

        return when {
            date.isToday() -> "Bugün ${date.toDateString(Constants.HHMM)}"
            date.addDay(1).isToday() -> "Dün ${date.toDateString(Constants.HHMM)}"
            date.addDay(2).isToday() -> "Önceki Gün ${date.toDateString(Constants.HHMM)}"
            difdays <= 7 -> "Bu Hafta"
            else -> date.toDateString(Constants.UI_DATE_PATTERN)
        }
    }

}