package com.zsk.androtweet.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kaloglu.library.ui.utils.Resource
import com.zsk.androtweet.interfaces.TimelineRepository
import com.zsk.androtweet.models.Tweet

class TimelineRepositoryImpl() : TimelineRepository {

    override val justTweets: Resource<List<Tweet>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val retweeted: Resource<List<Tweet>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val mentions: Resource<List<Tweet>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun insert(entity: Tweet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(entity: Tweet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(entity: Tweet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(): LiveData<Resource<Tweet>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val result: MutableLiveData<Resource<Tweet>> = MutableLiveData()

    fun setRemoved(entity: Tweet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
