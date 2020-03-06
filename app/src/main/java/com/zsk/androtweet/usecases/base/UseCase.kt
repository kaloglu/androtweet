package com.zsk.androtweet.usecases.base

import com.kaloglu.library.ui.interfaces.Repository

interface UseCase<T : Repository<*>> {
    val repository: T
}