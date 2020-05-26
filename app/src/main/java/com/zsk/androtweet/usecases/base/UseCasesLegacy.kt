package com.zsk.androtweet.usecases.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData

object None : Any()

/**
 * Simple Legacy methods without coroutine
 * */
abstract class UseCaseLegacy<R, in P> : LifecycleObserver where R : Any {
    protected abstract fun execute(param: P): R

    open operator fun invoke(params: P) = execute(params)

    open fun addLifecycle(lifecycle: Lifecycle) = lifecycle.addObserver(this)
    open fun removeLifecycle(lifecycle: Lifecycle) = lifecycle.removeObserver(this)
    open fun registerLifecycle(lifecycle: Lifecycle) {
        removeLifecycle(lifecycle)
        addLifecycle(lifecycle)
    }

}

/**
 * Simple Legacy methods without coroutine
 * */
abstract class UseCaseAsLiveData<R, in P> : UseCaseLegacy<LiveData<R>, P>() where R : Any {
    abstract override fun execute(param: P): LiveData<R>
}

/**
 * Simple Legacy methods without coroutine
 * */
abstract class UseCaseAsLiveDataNoParam<R> : UseCaseAsLiveData<R, None>() where R : Any {
    operator fun invoke() = invoke(None)
    final override fun execute(param: None) = execute()
    protected abstract fun execute(): LiveData<R>
}

/**
 * Simple Legacy methods without coroutine
 * */
abstract class UseCaseLegacyNoParams<R> : UseCaseLegacy<R, None>() where R : Any {
    operator fun invoke() = invoke(None)
    final override fun execute(param: None): R = execute()
    protected abstract fun execute(): R
}


