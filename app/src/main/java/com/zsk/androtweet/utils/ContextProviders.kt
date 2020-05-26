package com.zsk.androtweet.utils

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class ContextProviders {
    open val Main: CoroutineContext = Dispatchers.Main
    open val IO: CoroutineContext = Dispatchers.IO

    companion object {
        @Volatile
        private lateinit var INSTANCE: ContextProviders

        val instance: ContextProviders
            get() {
                synchronized(this) {
                    if (!::INSTANCE.isInitialized)
                        INSTANCE = ContextProviders()
                }
                return INSTANCE
            }
    }
}