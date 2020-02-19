package com.zsk.androtweet.interfaces

import com.kaloglu.library.ui.BaseModel
import com.zsk.androtweet.models.Resource

interface Repository<E : BaseModel> {
    val remote: Database
    val cache: Database

    fun insert(entity: E)

    fun insertAll(entities: List<E>)

    fun delete(entities: List<E>)

    fun deleteAll()

    fun setRemoved(entity: E)

    val getAll: Resource<List<E>>
}