package com.zsk.androtweet.repositories

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.zsk.androtweet.utils.twitter.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.Executor

abstract class BoundaryCallback<RequestType, ResultType>(
        private val executor: Executor = Dispatchers.Main.asExecutor()
) : PagedList.BoundaryCallback<RequestType>() {

    var retry: Runnable? = null
    val networkState = MutableLiveData<Resource<ResultType>>()

    @MainThread
    override fun onZeroItemsLoaded() {
        Log.d("BoundaryCallback", "onZeroItemsLoaded")
        fetchFromNetwork(true, null)
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: RequestType) {
        Log.d("BoundaryCallback", "onItemAtEndLoaded")
        fetchFromNetwork(false, itemAtEnd)
    }

    @MainThread
    private fun fetchFromNetwork(isInitial: Boolean, itemAtEnd: RequestType?) {
        networkState.postValue(Resource.loading())
        val apiResponse = createCall(itemAtEnd)

        val observer = object : Observer<ResultType> {
            override fun onChanged(response: ResultType) {
                apiResponse.removeObserver(this)

                if (isSuccessful(response)) {
                    retry = null
                    networkState.postValue(Resource.success(response))
                    saveCallResult(processResponse(response))
                } else {
                    retry = Runnable { fetchFromNetwork(isInitial, itemAtEnd) }
                    networkState.postValue(Resource.error(getErrorMessage(response)))
                }
            }
        }

        apiResponse.observeForever(observer)
    }

    protected open fun getErrorMessage(response: ResultType?): String {
        return "Error"
    }

    protected abstract fun isSuccessful(response: ResultType): Boolean

    protected open fun processResponse(response: ResultType?): ResultType {
        return response!!
    }

    @WorkerThread
    protected fun saveCallResult(result: ResultType) = Unit

    @MainThread
    protected abstract fun createCall(itemAtEnd: RequestType?): LiveData<ResultType>

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        if (prevRetry != null) {
            executor.execute(prevRetry)
        }
    }
}