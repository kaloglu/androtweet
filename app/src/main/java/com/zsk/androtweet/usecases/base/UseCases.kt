package com.zsk.androtweet.usecases.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.zsk.androtweet.utils.ContextProviders
import com.zsk.androtweet.utils.twitter.TwitterResultBoundResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * new Coroutine methods
 * */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class UseCase<DomainModel, DataModel, Param>(contextProviders: ContextProviders = ContextProviders.instance)
    : TwitterResultBoundResource<DomainModel, DataModel>(contextProviders), LifecycleObserver
        where DomainModel : Any, DataModel : Any, Param : UseCase.Request {

    interface Request

    val useCaseTag = this.javaClass.simpleName

    abstract var request: Param

    open fun addLifecycle(lifecycle: Lifecycle) = lifecycle.addObserver(this)

    open fun removeLifecycle(lifecycle: Lifecycle) = lifecycle.removeObserver(this)

    open fun registerLifecycle(lifecycle: Lifecycle) {
        removeLifecycle(lifecycle)
        addLifecycle(lifecycle)
    }

}
