package com.zsk.androtweet.interfaces

import com.zsk.androtweet.models.Resource
import com.zsk.androtweet.models.Tweet

interface TimelineRepository : Repository<Tweet> {

    val justTweets: Resource<List<Tweet>>

    val retweeted: Resource<List<Tweet>>

    val mentions: Resource<List<Tweet>>
}
