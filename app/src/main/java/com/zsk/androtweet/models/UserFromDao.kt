package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.kaloglu.library.ui.BaseModel

@Suppress("CovariantEquals")
@Entity(tableName = "user", primaryKeys = ["user_id"])
data class UserFromDao(
        var secret: String? = "",
        var token: String? = "",
        var screenName: String? = null,
        @ColumnInfo(name = "user_id")
        var id: Long = 0,
        var idStr: String? = null
) : BaseModel {

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = getID<Long>() == obj2.getID<Long>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T
    //endregion

}
