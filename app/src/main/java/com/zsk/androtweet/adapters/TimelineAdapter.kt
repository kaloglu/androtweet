package com.zsk.androtweet.adapters

import android.view.ViewGroup
import com.kaloglu.library.BaseRecyclerAdapter
import com.kaloglu.library.inflate
import com.zsk.androtweet.R
import com.zsk.androtweet.adapters.viewholders.TweetViewHolder
import com.zsk.androtweet.models.Tweet

//
// Created by  on 2020-02-02.
//
class TimelineAdapter : BaseRecyclerAdapter<Tweet, TweetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            TweetViewHolder(parent.inflate(R.layout.timeline_tweets, false))
}