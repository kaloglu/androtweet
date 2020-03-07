package com.zsk.androtweet.usecases

import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.base.BaseUseCaseNoParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class GetUserUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseUseCaseNoParams<User>() {

    override suspend fun run() = repository.get()
}
