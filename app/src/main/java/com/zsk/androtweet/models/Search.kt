package com.zsk.androtweet.models

class Search {
    @JvmField
    var lastTweetId: Long = 0
    var isViewMentions = true
    var isViewMyTweets = true
    var isViewRTs = true

    companion object {
        @JvmStatic
        var INSTANCE = Search()
    }
}