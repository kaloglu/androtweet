package com.zsk.androtweet.simpletasks

import android.os.AsyncTask
import com.kaloglu.library.ui.BaseModel
import com.zsk.androtweet.database.dao.base.BaseDao
import com.zsk.androtweet.interfaces.Repository
import com.zsk.androtweet.models.Resource

class SimpleInsert<E : BaseModel, DAO : BaseDao<E>, R : Repository<E>> internal constructor(
        private val delegate: R,
        private val dao: DAO
) : AsyncTask<E, Void, Resource<E>>() {

    override fun doInBackground(vararg params: E): Resource<E> {
        if (params.isNotEmpty()) {
            dao.insert(params[0])
            return Resource.Success(params[0])
        }
        return Resource.Failure()
    }

    override fun onPostExecute(result: Resource<E>) {
        delegate.asyncFinished(result)
    }

}

class SimpleUpdate<E : BaseModel, DAO : BaseDao<E>, R : Repository<E>> internal constructor(
        private val delegate: R,
        private val dao: DAO
) : AsyncTask<E, Void, Resource<E>>() {
    override fun doInBackground(vararg params: E): Resource<E> {
        if (params.isNotEmpty()) {
            dao.update(params[0])
            return Resource.Success(params[0])
        }
        return Resource.Failure()
    }

    override fun onPostExecute(result: Resource<E>) {
        delegate.asyncFinished(result)
    }

}

class SimpleDelete<E : BaseModel, DAO : BaseDao<E>, R : Repository<E>> internal constructor(
        private val delegate: R,
        private val dao: DAO
) : AsyncTask<E, Void, Resource<E>>() {
    override fun doInBackground(vararg params: E): Resource<E> {
        if (params.isNotEmpty()) {
            dao.delete(params[0])
            return Resource.Success(params[0])
        }
        return Resource.Failure()
    }

    override fun onPostExecute(result: Resource<E>) {
        delegate.asyncFinished(result)
    }

}

//class SimpleGet<E : BaseModel, DAO : BaseDao<E>, R : Repository<E>> internal constructor(
//        private val delegate: R,
//        private val dao: DAO
//) : AsyncTask<Void, Void, Resource<E>>() {
//    override fun doInBackground(vararg params: Void): Resource.Success<E> {
//        return Resource.Success(dao.get())
//    }
//
//    override fun onPostExecute(result: Resource<E>) = delegate.asyncFinished(result)
//
//}

class SimpleDeleteAll<DAO : BaseDao<*>, R : Repository<*>> internal constructor(
        private val delegate: R,
        private val dao: DAO
) : AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void): Boolean {
        dao.deleteAll()
        return true
    }

    override fun onPostExecute(result: Boolean) {
        delegate.asyncFinished(null)
    }

}