package com.zsk.androtweet.utils

import androidx.lifecycle.LiveData

/**
 * A LiveData class that has `null` value.
 */
class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {
    companion object {
        fun <T> create(): AbsentLiveData<T> = AbsentLiveData()
    }
}