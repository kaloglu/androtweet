package com.zsk.androtweet.utils

import androidx.room.TypeConverter
import com.kaloglu.library.ktx.currentTimestamp
import com.kaloglu.library.ktx.toDate
import com.kaloglu.library.ktx.toDateString
import com.zsk.androtweet.models.ListType
import java.util.*

object Converters {

    @TypeConverter
    @JvmStatic
    fun stringToDate(value: String?): Date = value.toDate(Constants.UI_DATE_PATTERN)

    @TypeConverter
    @JvmStatic
    fun dateToString(date: Date): String = date.toDateString(Constants.UI_DATE_PATTERN)

    @TypeConverter
    fun longToDate(value: Long = currentTimestamp()): Date = Date(value)

    @TypeConverter
    fun dateToLong(date: Date = Date()): Long = date.time

    @TypeConverter
    @JvmStatic
    fun stringToListType(value: String = ListType.TWEETS.name): ListType = ListType.valueOf(value)

    @TypeConverter
    @JvmStatic
    fun listTypeToString(listType: ListType): String = listType.name
}