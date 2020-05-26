package com.zsk.androtweet.utils

import androidx.room.TypeConverter
import com.kaloglu.library.ktx.toDate
import com.kaloglu.library.ktx.toDateString
import java.util.*

object Converters {

    @TypeConverter
    @JvmStatic
    fun stringToDate(value: String?) = value.toDate(Constants.ROOM_DATE_PATTERN)

    @TypeConverter
    @JvmStatic
    fun dateToString(date: Date): String = date.toDateString(Constants.ROOM_DATE_PATTERN)

}