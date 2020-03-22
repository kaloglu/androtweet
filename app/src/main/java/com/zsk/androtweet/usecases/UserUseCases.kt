package com.zsk.androtweet.usecases

import android.os.AsyncTask
import androidx.lifecycle.Lifecycle
import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.base.BaseLiveDataUseCaseNoParam
import com.zsk.androtweet.usecases.base.BaseUseCase
import com.zsk.androtweet.usecases.base.BaseUseCaseNoParams

class GetUserLiveDataUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseLiveDataUseCaseNoParam<User>() {
    override fun execute() = repository.getDUC()
    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }
}

class AddUserUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseUseCase<Unit, User>() {
    override fun execute(param: User) {
        InsertAsyncTask(repository).execute(param)
    }

    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }

    private class InsertAsyncTask internal constructor(val repository: UserRepository) : AsyncTask<User, Unit, Unit>() {
        override fun doInBackground(vararg params: User) = repository.insert(params[0])
    }
}

class ClearUserUseCase(
        private val repository: UserRepository = UserRepository.getInstance()
) : BaseUseCaseNoParams<Unit>() {
    override fun execute() {
        DeleteAsyncTask(repository).execute()
    }

    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }

    private class DeleteAsyncTask internal constructor(val repository: UserRepository) : AsyncTask<User, Unit, Unit>() {
        override fun doInBackground(vararg params: User) = repository.clean()
    }
}
