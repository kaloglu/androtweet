package com.zsk.androtweet.utils

import com.kaloglu.library.ktx.GenericExtensions
import com.kaloglu.library.ktx.isToday
import java.text.SimpleDateFormat
import java.util.*

object Constants {
    private const val TWEET_DATE_PATTERN: String = "E MMM dd HH:mm:ss Z yyyy"

    const val UNKNOWN_ERROR_CODE = "-1"
    const val UNKNOWN_ERROR = "Unknown Error"

    //ROOM
    const val ROOM_EXCEPTION_CODE: String = "350"
    const val ROOM_EXCEPTION_NON_PARAMS_CODE: String = "351"
    const val ROOM_EXCEPTION_NON_PARAMS: String = "Not found any params!"

    fun String.isToday() = toDate(TWEET_DATE_PATTERN)?.isToday() ?: false
    fun Long.isToday() = toDate(TWEET_DATE_PATTERN).isToday()

}

fun String.toDate(datePattern: String = GenericExtensions.DateStringPattern) =
        SimpleDateFormat(datePattern, Locale.getDefault()).parse(this)

fun Long.toDate(datePattern: String = GenericExtensions.DateStringPattern) = Date(this)
