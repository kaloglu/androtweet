package com.zsk.androtweet.usecases

import android.os.AsyncTask
import androidx.lifecycle.Lifecycle
import com.zsk.androtweet.models.User
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.usecases.base.UseCaseAsLiveDataNoParam
import com.zsk.androtweet.usecases.base.UseCaseLegacy
import com.zsk.androtweet.usecases.base.UseCaseLegacyNoParams

class GetUserUseCaseAsLiveData(private val repository: UserRepository = UserRepository.getInstance())
    : UseCaseAsLiveDataNoParam<User>() {

    override fun execute() = repository.getDUC()

    override fun registerLifecycle(lifecycle: Lifecycle) {
        super.registerLifecycle(lifecycle)
        repository.registerLifecycle(lifecycle)
    }

}

class AddUserUseCaseLegacy(private val repository: UserRepository = UserRepository.getInstance()) : UseCaseLegacy<Unit, User>() {

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

class ClearUserUseCaseLegacy(private val repository: UserRepository = UserRepository.getInstance()) : UseCaseLegacyNoParams<Unit>() {

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
