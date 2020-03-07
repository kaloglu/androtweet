package com.zsk.androtweet.usecases

import androidx.lifecycle.Transformations
import com.kaloglu.library.ui.utils.Resource
import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.usecases.base.UseCase

class GetUserUseCases(override val repository: UserRepository, private val postState: (LoginState) -> Unit) : UseCase<UserRepository> {
    operator fun invoke() {
        Transformations.map(repository.get()) {
            when (it) {
                is Resource.Success -> postState(LoginState.Authenticated(it.body))
                else -> postState(LoginState.UnAuthenticated())
            }
        }
    }
}

class InsertUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke(entity: User) {
        repository.insert(entity)
    }
}

class RemoveUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke(entity: User) = repository.delete(entity)
}

class UpdateUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke(entity: User) = repository.update(entity)
}

class CleanUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke() {
        repository.clean()
    }
}
