package com.zsk.androtweet.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kaloglu.library.ui.BaseModel
import com.zsk.androtweet.models.Resource

interface Repository<E : BaseModel> {

    fun insert(entity: E)

    fun delete(entity: E)

    fun update(entity: E)

    fun get(): LiveData<E>

    val result: MutableLiveData<Resource<E>>

    fun asyncFinished(resource: Resource<E>?) {
        result.value = resource
    }
}