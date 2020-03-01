package com.zsk.androtweet.usecases

import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.base.UseCase

class GetUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke() = repository.get()
}

class InsertUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke(entity: User) = repository.insert(entity)
}

class RemoveUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke(entity: User) = repository.delete(entity)
}

class UpdateUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke(entity: User) = repository.update(entity)
}

class CleanUserUseCases(override val repository: UserRepository) : UseCase<UserRepository> {
    operator fun invoke() = repository.clean()
}
