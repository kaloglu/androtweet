package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaloglu.library.ui.BaseModel
import twitter4j.Status

@Entity(tableName = "tweets")
class Tweet : BaseModel {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0
    @ColumnInfo(name = "reply_id")
    var replyId: Long = 0
    @ColumnInfo(name = "time")
    var time: Long = 0
    @ColumnInfo(name = "text")
    var tweetText: String? = null
    @ColumnInfo(name = "fav_count")
    var favcount = 0
    @ColumnInfo(name = "rt_count")
    var rtCount = 0
    @ColumnInfo(name = "isRemoved")
    var isRemoved = false
    @ColumnInfo(name = "username")
    var username = ""

    var isSelected = false

    constructor()

    constructor(status: Status) {
        id = status.id
        tweetText = status.text
        replyId = status.inReplyToStatusId
        time = status.createdAt.time
        rtCount = status.retweetCount
        favcount = status.favoriteCount
        username = status.user.name
    }

    override fun <T : BaseModel> equals(obj2: T) = false

    override fun <T : Any> getId() = id as T

}