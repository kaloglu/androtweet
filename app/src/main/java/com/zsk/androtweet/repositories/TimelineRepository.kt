package com.zsk.androtweet.repositories

import com.zsk.androtweet.database.TimeLineCacheDatabase
import com.zsk.androtweet.database.TimeLineRemoteDatabase
import com.zsk.androtweet.interfaces.TimelineRepository
import com.zsk.androtweet.models.Resource
import com.zsk.androtweet.models.Tweet

class TimelineRepositoryImpl(
        override val remote: TimeLineRemoteDatabase,
        override val cache: TimeLineCacheDatabase
) : TimelineRepository {

    override val justTweets: Resource<List<Tweet>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val retweeted: Resource<List<Tweet>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val mentions: Resource<List<Tweet>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun insert(entity: Tweet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertAll(entities: List<Tweet>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(entities: List<Tweet>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRemoved(entity: Tweet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val getAll: Resource<List<Tweet>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}
