package com.zsk.androtweet.interfaces

import com.zsk.androtweet.models.Resource
import com.zsk.androtweet.models.Tweet

interface UseCase<T : Repository<*>> {
    val repository: T
}

interface GetTweetsUseCases : UseCase<TimelineRepository> {
    operator fun invoke(): Resource<List<Tweet>>
}

interface RemoveTweetsUseCases : UseCase<TimelineRepository> {
    operator fun invoke(entity: Tweet)
}

interface InsertTweetsUseCases : UseCase<TimelineRepository> {
    operator fun invoke(entities: List<Tweet>)
}
