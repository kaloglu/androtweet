package com.zsk.androtweet.usecases.base

import com.kaloglu.library.ui.BaseModel
import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.ui.utils.Constants
import com.kaloglu.library.ui.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlin.coroutines.CoroutineContext

object None : Any()

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseFlowUseCase<E, in P> where E : BaseModel {

    protected abstract suspend fun run(params: P): Flow<E>

    open operator fun invoke(viewModelScope: CoroutineScope, resultChannel: ConflatedBroadcastChannel<Resource<*>>, params: P) {
        viewModelScope.launch {
            run(params)
                    .mapLatest {
                        resultChannel.send(
                                Resource.Success(it)
                        )
                    }
                    .catch { throwable ->
                        resultChannel.send(
                                Resource.Failure(
                                        ErrorModel(
                                                Constants.UNKNOWN_ERROR_CODE,
                                                throwable.localizedMessage
                                                        ?: Constants.UNKNOWN_ERROR
                                        )
                                )
                        )
                    }
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseFlowUseCaseNoParams<E> : BaseFlowUseCase<E, None>() where E : BaseModel {

    operator fun invoke(viewModelScope: CoroutineScope, resultChannel: ConflatedBroadcastChannel<Resource<*>>) = invoke(viewModelScope, resultChannel, None)

    final override suspend fun run(params: None) = run()

    protected abstract suspend fun run(): Flow<E>

}

@ExperimentalCoroutinesApi
abstract class BaseUseCase<E, in P> : CoroutineScope where E : BaseModel {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    protected abstract suspend fun run(params: P)

    open operator fun invoke(params: P) {
        launch {
            run(params)
        }
    }
}

@ExperimentalCoroutinesApi
abstract class BaseUseCaseNoParams<E> : BaseUseCase<E, None>() where E : BaseModel {

    operator fun invoke() = invoke(None)

    final override suspend fun run(params: None) = run()

    protected abstract suspend fun run()

}
