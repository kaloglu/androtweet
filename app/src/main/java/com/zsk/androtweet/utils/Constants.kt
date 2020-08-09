package com.zsk.androtweet.utils

import com.kaloglu.library.ui.models.ErrorModel

object Constants {

    const val NEED_LOGIN_ERROR_CODE = "401"
    const val prefetchDistance = 30
    const val pageSize = 50

    const val TWEET_DATE_PATTERN: String = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
    const val UI_DATE_PATTERN: String = "dd MMM yyyy HH:mm"
    const val HHMM: String = "HH:mm"

    const val UNKNOWN_ERROR_CODE = "-1"

    const val UNKNOWN_ERROR = "Unknown Error"

    //ROOM
    const val ROOM_EXCEPTION_CODE: String = "350"
    const val ROOM_EXCEPTION_NON_PARAMS_CODE: String = "351"

    const val ROOM_EXCEPTION_NON_PARAMS: String = "Not found any params!"

    val LoginError by lazy { ErrorModel(NEED_LOGIN_ERROR_CODE, NEED_LOGIN_MESSAGE) }

    private const val NEED_LOGIN_MESSAGE = "You should be logged in!"

}
