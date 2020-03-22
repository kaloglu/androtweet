package com.zsk.androtweet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.kaloglu.library.ui.BaseModel
import com.twitter.sdk.android.core.models.User

@Suppress("CovariantEquals")
@Entity(tableName = "user", primaryKeys = ["user_id"])
class User : BaseModel {
    var secret: String? = ""
    var token: String? = ""

    @ColumnInfo(name = "user_id")
    var id: Long = 0
    var idStr: String? = null
    var name: String? = null
    var createdAt: String? = null
    var description: String? = null
    var email: String? = null
    var contributorsEnabled: Boolean = false
    var defaultProfile: Boolean = false
    var defaultProfileImage: Boolean = false
    var favouritesCount: Int = 0
    var followRequestSent: Boolean = false
    var followersCount: Int = 0
    var friendsCount: Int = 0
    var geoEnabled: Boolean = false
    var isTranslator: Boolean = false
    var lang: String? = null
    var listedCount: Int = 0
    var location: String? = null
    var profileBackgroundColor: String? = null
    var profileBackgroundImageUrl: String? = null
    var profileBackgroundImageUrlHttps: String? = null
    var profileBackgroundTile: Boolean = false
    var profileBannerUrl: String? = null
    var profileImageUrl: String? = null
    var profileImageUrlHttps: String? = null
    var profileLinkColor: String? = null
    var profileSidebarBorderColor: String? = null
    var profileSidebarFillColor: String? = null
    var profileTextColor: String? = null
    var profileUseBackgroundImage: Boolean = false
    var protectedUser: Boolean = false
    var screenName: String? = null
    var showAllInlineMedia: Boolean = false
    var statusesCount: Int = 0
    var timeZone: String? = null
    var url: String? = null
    var utcOffset: Int = 0
    var verified: Boolean = false
    var withheldScope: String? = null

    constructor()
    constructor(data: User, token: String? = "", secret: String? = "") {
        id = data.id
        idStr = data.idStr
        name = data.name
        createdAt = data.createdAt
        description = data.description
        email = data.email
        contributorsEnabled = data.contributorsEnabled
        defaultProfile = data.defaultProfile
        defaultProfileImage = data.defaultProfileImage
        favouritesCount = data.favouritesCount
        followRequestSent = data.followRequestSent
        followersCount = data.followersCount
        friendsCount = data.friendsCount
        geoEnabled = data.geoEnabled
        isTranslator = data.isTranslator
        lang = data.lang
        listedCount = data.listedCount
        location = data.location
        profileBackgroundColor = data.profileBackgroundColor
        profileBackgroundImageUrl = data.profileBackgroundImageUrl
        profileBackgroundImageUrlHttps = data.profileBackgroundImageUrlHttps
        profileBackgroundTile = data.profileBackgroundTile
        profileBannerUrl = data.profileBannerUrl
        profileImageUrl = data.profileImageUrl
        profileImageUrlHttps = data.profileImageUrlHttps
        profileLinkColor = data.profileLinkColor
        profileSidebarBorderColor = data.profileSidebarBorderColor
        profileSidebarFillColor = data.profileSidebarFillColor
        profileTextColor = data.profileTextColor
        profileUseBackgroundImage = data.profileUseBackgroundImage
        protectedUser = data.protectedUser
        screenName = data.screenName
        showAllInlineMedia = data.showAllInlineMedia
        statusesCount = data.statusesCount
        timeZone = data.timeZone
        url = data.url
        utcOffset = data.utcOffset
        verified = data.verified
        withheldScope = data.withheldScope
        this.token = token
        this.secret = secret
    }

    //region BaseModel
    override fun <T : BaseModel> equals(obj2: T) = getID<Long>() == obj2.getID<Long>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getID() = id as T
    //endregion
}