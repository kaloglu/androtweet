package com.zsk.androtweet.usecases

import androidx.lifecycle.Lifecycle
import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.base.BaseLiveDataUseCaseNoParam
import com.zsk.androtweet.usecases.base.BaseUseCase

class GetUserFlowUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseLiveDataUseCaseNoParam<User>() {
    override fun execute() = repository.get()
    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }
}

class AddUserUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseUseCase<Boolean, User>() {
    override fun execute(param: User): Boolean {
        return try {
            repository.insert(param)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }
}
