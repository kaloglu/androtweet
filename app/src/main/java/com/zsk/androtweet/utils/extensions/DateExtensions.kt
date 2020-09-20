@file:JvmName("KtxDateUtilLocal")

package com.zsk.androtweet.utils.extensions

import com.kaloglu.library.ktx.toDate
import com.zsk.androtweet.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

fun String?.toTimeStamp(
        datePattern: String = Constants.TWEET_DATE_PATTERN,
        locale: Locale = Locale.getDefault()
) = toDate(datePattern, locale).time

fun String?.toDate(
        datePattern: String = Constants.TWEET_DATE_PATTERN,
        locale: Locale = Locale.getDefault()
) =
        this?.let { SimpleDateFormat(datePattern, locale).parse(this) }
                ?: Date()
