package com.zsk.androtweet.models

import android.database.Cursor
import twitter4j.Status

class Tweet {
    var favcount = 0
    var id: Long = 0
    var rtCount = 0
    var replyId: Long = 0
    var time: Long = 0
    var tweetText: String? = null

    constructor(paramCursor: Cursor) {
        id = paramCursor.getLong(paramCursor.getColumnIndex("tweet_id"))
        tweetText = paramCursor.getString(paramCursor.getColumnIndex("tweet"))
        replyId = paramCursor.getLong(paramCursor.getColumnIndex("reply_id"))
        time = paramCursor.getLong(paramCursor.getColumnIndex("tweettime"))
        rtCount = paramCursor.getInt(paramCursor.getColumnIndex("RT"))
        favcount = paramCursor.getInt(paramCursor.getColumnIndex("FAV"))
    }

    constructor(paramStatus: Status) {
        id = paramStatus.id
        tweetText = paramStatus.text
        replyId = paramStatus.inReplyToStatusId
        time = paramStatus.createdAt.time
        rtCount = paramStatus.retweetCount
        favcount = paramStatus.favoriteCount
    }

}