package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.kaloglu.library.ktx.currentTimestamp
import com.kaloglu.library.ktx.toDate
import com.kaloglu.library.ktx.toDateString
import com.kaloglu.library.ui.BaseModel
import com.zsk.androtweet.utils.Constants
import java.util.*
import com.twitter.sdk.android.core.models.User as SdkUser

@Suppress("CovariantEquals")
@Entity(tableName = "user", primaryKeys = ["user_id"])
data class User(
        var secret: String? = "",
        var token: String? = "",

        @ColumnInfo(name = "user_id")
        var id: Long = 0,
        var idStr: String? = null,
        var name: String? = null,
        var description: String? = null,
        var email: String? = null,
        var favouritesCount: Int = 0,
        var followRequestSent: Boolean = false,
        var followersCount: Int = 0,
        var friendsCount: Int = 0,
        var listedCount: Int = 0,
        var screenName: String? = null,
        var statusesCount: Int = 0,
        var verified: Boolean = false,
        var profileBannerUrl: String? = null,
        var profileImageUrl: String? = null,
        var profileImageUrlHttps: String? = null,
        var protectedUser: Boolean = false,
        var url: String? = null,
        var location: String? = null,
        var geoEnabled: Boolean = false,
        var lang: String? = null,
        var createdAt: Date = currentTimestamp().toDateString(Constants.TWEET_DATE_PATTERN).toDate(Constants.TWEET_DATE_PATTERN),
        var profileBackgroundColor: String? = null,
        var profileBackgroundImageUrl: String? = null,
        var profileBackgroundImageUrlHttps: String? = null,
        var profileBackgroundTile: Boolean = false,
        var profileLinkColor: String? = null,
        var profileSidebarBorderColor: String? = null,
        var profileSidebarFillColor: String? = null,
        var profileTextColor: String? = null,
        var profileUseBackgroundImage: Boolean = false
) : BaseModel {
    constructor(data: SdkUser, token: String? = "", secret: String? = "") : this(
            token = token,
            secret = secret,
            id = data.id,
            idStr = data.idStr,
            name = data.name,
            description = data.description,
            email = data.email,
            favouritesCount = data.favouritesCount,
            followRequestSent = data.followRequestSent,
            followersCount = data.followersCount,
            friendsCount = data.friendsCount,
            listedCount = data.listedCount,
            screenName = data.screenName,
            statusesCount = data.statusesCount,
            verified = data.verified,
            profileBannerUrl = data.profileBannerUrl,
            profileImageUrl = data.profileImageUrl,
            profileImageUrlHttps = data.profileImageUrlHttps,
            protectedUser = data.protectedUser,
            url = data.url,
            location = data.location,
            geoEnabled = data.geoEnabled,
            lang = data.lang,
            createdAt = data.createdAt.toDate(Constants.TWEET_DATE_PATTERN),
            profileBackgroundColor = data.profileBackgroundColor,
            profileBackgroundImageUrl = data.profileBackgroundImageUrl,
            profileBackgroundImageUrlHttps = data.profileBackgroundImageUrlHttps,
            profileBackgroundTile = data.profileBackgroundTile,
            profileLinkColor = data.profileLinkColor,
            profileSidebarBorderColor = data.profileSidebarBorderColor,
            profileSidebarFillColor = data.profileSidebarFillColor,
            profileTextColor = data.profileTextColor,
            profileUseBackgroundImage = data.profileUseBackgroundImage
    )

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = getID<Long>() == obj2.getID<Long>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T
    //endregion

}
