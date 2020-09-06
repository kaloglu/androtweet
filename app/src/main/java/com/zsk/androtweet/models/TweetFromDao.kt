package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.kaloglu.library.databinding4vm.model.RecyclerBindableItem
import com.kaloglu.library.ktx.currentTimestamp
import com.kaloglu.library.ui.BaseModel
import com.zsk.androtweet.R

@Suppress("CovariantEquals")
@Entity(
        tableName = "tweets",
        primaryKeys = ["tweet_id"]
)
data class TweetFromDao(
        @ColumnInfo(name = "tweet_id")
        var id: Long = 0,
        @ColumnInfo(name = "tweet_id_str")
        var idStr: String? = null,
        @ColumnInfo(name = "tweet_user_id")
        var userId: Long? = null,
        var text: String? = "test",
        var favoriteCount: Int? = null,
        var favorited: Boolean = false,
        var retweetCount: Int = 0,
        var retweeted: Boolean = false,
        var inReplyToScreenName: String? = null,
        var inReplyToStatusId: Long = 0,
        var inReplyToStatusIdStr: String? = null,
        var inReplyToUserId: Long = 0,
        var inReplyToUserIdStr: String? = null,
        var quotedStatusIdStr: String? = null,
        var lang: String? = null,
        var source: String? = null,
        var createdAt: Long = currentTimestamp(),
        var cachedAt: Long = currentTimestamp(),
        var isDeleted: Boolean = false,
        override var layoutId: Int = LAYOUT_ID

) : RecyclerBindableItem(), BaseModel {

    @Ignore
    var isSelected: Boolean = false

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = false

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T
    //endregion

    companion object {
        const val LAYOUT_ID = R.layout.tweet_layout
    }
}
