package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.kaloglu.library.ktx.currentTimestamp
import com.kaloglu.library.ktx.toDate
import com.kaloglu.library.ui.BaseModel
import com.zsk.androtweet.utils.Constants
import java.util.*
import com.twitter.sdk.android.core.models.User as SdkUser

@Suppress("CovariantEquals")
@Entity(tableName = "user", primaryKeys = ["user_id"])
data class User(
        var secret: String? = "",
        var token: String? = "",
        var screenName: String? = null,
        @ColumnInfo(name = "user_id")
        var id: Long = 0,
        var idStr: String? = null
) : BaseModel {
    constructor(data: SdkUser, token: String? = "", secret: String? = "") : this(
            token = token,
            secret = secret,
            id = data.id,
            idStr = data.idStr,
            screenName = data.screenName
    )

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = getID<Long>() == obj2.getID<Long>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T
    //endregion

}
