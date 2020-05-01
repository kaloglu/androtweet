package com.zsk.androtweet.utils.twitter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.zsk.androtweet.utils.ContextProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
abstract class TwitterResultBoundResource<ResultType : Any, RequestType : Any>(private val contextProviders: ContextProviders) {
    lateinit var coroutineScope: CoroutineScope

    val flow get() = result.asFlow()

    private val result = MutableLiveData<Resource<ResultType>>()

    protected fun execute() {
        setValue(Resource.loading(null))
        loadFromDb()
                .flowOn(contextProviders.Main)
                .onEach {
                    when (it) {
                        it is Collection<*> && it.isEmpty() -> setValue(Resource.empty())
                        else -> setValue(Resource.success(it))
                    }
                    if (shouldFetch(it)) fetchFromNetwork()
                }.launchIn(coroutineScope)
    }

    private fun fetchFromNetwork() {
        createCall()
                .flowOn(contextProviders.IO)
                .onEach(this::saveCallResult)
                .launchIn(coroutineScope)
    }

    protected open fun onFetchFailed(message: String) {
        Log.e("onFetchFailed", message)
    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromDb(): Flow<ResultType>

    abstract suspend fun saveCallResult(result: RequestType)

    abstract fun createCall(): Flow<RequestType>

}
