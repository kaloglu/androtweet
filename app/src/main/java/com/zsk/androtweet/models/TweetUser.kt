package com.zsk.androtweet.models

import twitter4j.User

class TweetUser {
    var id: Long = 0
        private set
    var profileImageURL: String? = null
        private set
    var screenName: String? = null
        private set

    constructor() {}
    constructor(paramUser: User) {
        id = paramUser.id
        screenName = paramUser.screenName
        profileImageURL = paramUser.profileImageURL
    }

}