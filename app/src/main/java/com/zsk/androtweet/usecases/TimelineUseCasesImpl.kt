package com.zsk.androtweet.usecases

import com.zsk.androtweet.models.Tweet
import com.zsk.androtweet.interfaces.TimelineRepository
import com.zsk.androtweet.interfaces.GetTweetsUseCases
import com.zsk.androtweet.interfaces.InsertTweetsUseCases
import com.zsk.androtweet.interfaces.RemoveTweetsUseCases

class GetTweetsUseCasesImpl(override val repository: TimelineRepository) : GetTweetsUseCases {
    override fun invoke() = repository.getAll
}

class RemoveTweetsUseCasesImpl(override val repository: TimelineRepository) : RemoveTweetsUseCases {
    override fun invoke(entity: Tweet) = repository.setRemoved(entity)
}

class InsertTweetsUseCasesImpl(override val repository: TimelineRepository) : InsertTweetsUseCases {
    override fun invoke(entities: List<Tweet>) = repository.insertAll(entities)
}