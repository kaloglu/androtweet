package com.zsk.androtweet.usecases.base

import com.zsk.androtweet.interfaces.Repository

interface UseCase<T : Repository<*>> {
    val repository: T
}