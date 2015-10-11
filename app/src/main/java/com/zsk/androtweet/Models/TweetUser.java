package com.zsk.androtweet.Models;

import twitter4j.User;

/**
 * Created by zsk on 11.06.2015.
 */
public class TweetUser {
    protected long Id;
    protected String ScreenName;
    protected String ProfileImageURL;

    public TweetUser() {
    }

    public TweetUser(User user) {
        Id=user.getId();
        ScreenName=user.getScreenName();
        ProfileImageURL =user.getProfileImageURL();
    }

   /* public TweetUser(Cursor cursor) {
        Id=cursor.getLong(cursor.getColumnIndex(DB.Users.USER_ID));
        ScreenName=cursor.getString(cursor.getColumnIndex(DB.Users.SCREENNAME));
        ProfileImageURL=cursor.getString(cursor.getColumnIndex(DB.Users.PROFILEIMAGEURL));
    }*/

    public long getId() {
        return Id;
    }

    public String getScreenName() {
        return ScreenName;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

}
