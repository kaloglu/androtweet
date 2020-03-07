package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.kaloglu.library.ui.BaseModel
import com.kaloglu.library.ui.RecyclerItem
import twitter4j.Status

@Entity(tableName = "tweets", primaryKeys = ["id"])
class Tweet : BaseModel, RecyclerItem {
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
    @ColumnInfo(name = "is_removed")
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

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = false

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T
    //endregion

    //region Recycler Item

    override var layoutId: Int = -1

    //endregion
}