package com.zsk.androtweet.interfaces

import com.kaloglu.library.ui.interfaces.Repository
import com.kaloglu.library.ui.utils.Resource
import com.zsk.androtweet.models.Tweet

interface TimelineRepository : Repository<Tweet> {
    val justTweets: Resource<List<Tweet>>
    val retweeted: Resource<List<Tweet>>
    val mentions: Resource<List<Tweet>>
}
