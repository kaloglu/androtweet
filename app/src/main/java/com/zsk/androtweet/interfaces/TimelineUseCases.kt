package com.zsk.androtweet.interfaces

import com.zsk.androtweet.models.Resource
import com.zsk.androtweet.models.Tweet
import com.zsk.androtweet.usecases.base.UseCase

interface GetTweetsUseCases : UseCase<TimelineRepository> {
    operator fun invoke(): Resource<List<Tweet>>
}

interface RemoveTweetsUseCases : UseCase<TimelineRepository> {
    operator fun invoke(entity: Tweet)
}

interface InsertTweetsUseCases : UseCase<TimelineRepository> {
    operator fun invoke(entities: List<Tweet>)
}
