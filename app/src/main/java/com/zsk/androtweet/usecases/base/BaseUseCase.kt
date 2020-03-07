package com.zsk.androtweet.usecases.base

import com.kaloglu.library.ui.BaseModel
import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.ui.utils.Constants
import com.kaloglu.library.ui.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow

object None : Any()

@ExperimentalCoroutinesApi
abstract class BaseUseCase<E, in P> where E : BaseModel {
    lateinit var viewModelScope: CoroutineScope

    // region Members
    private val backgroundDispatcher = Dispatchers.IO

    val resultChannel = ConflatedBroadcastChannel<Resource<*>>()

    // endregion

    protected abstract suspend fun run(params: P): Flow<Resource<E>>

    open operator fun invoke(params: P) {
        launchDataLoad(params) { resultChannel.offer(it) }
    }

    private fun launchDataLoad(params: P, onErrorBlock: (Resource<*>) -> Unit
    ): Job {
        return viewModelScope.launch {
            try {
                run(params)
            } catch (error: Throwable) {
                onErrorBlock(
                        Resource.Failure(
                                ErrorModel(
                                        Constants.UNKNOWN_ERROR_CODE,
                                        error.localizedMessage ?: Constants.UNKNOWN_ERROR
                                )
                        )
                )
            }
        }
    }

}

@ExperimentalCoroutinesApi
abstract class BaseUseCaseNoParams<E> : BaseUseCase<E, None>() where E : BaseModel {

    operator fun invoke() = invoke(None)

    final override suspend fun run(params: None) = run()

    protected abstract suspend fun run(): Flow<Resource<E>>

}