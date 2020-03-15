package com.zsk.androtweet.usecases

import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.base.BaseFlowUseCaseNoParams
import com.zsk.androtweet.usecases.base.BaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class GetUserFlowUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseFlowUseCaseNoParams<User>() {

    override suspend fun run(): Flow<User> = repository.get()
}

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AddUserUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseUseCase<User, User>() {

    override suspend fun run(params: User) {
        repository.insert(params)
    }
}
