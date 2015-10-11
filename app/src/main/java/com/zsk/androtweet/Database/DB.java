package com.zsk.androtweet.Database;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.zsk.androtweet.Models.Tweet;

/**
 * Created by zsk on 11.06.2015.
 */
public class DB {

    /**
      DB EXPLANATION
     * */
    protected static final int DBVERSION = 7;
    protected static final String DBNAME = "AndroTweet";
    protected static final String DROP_QUERY ="DROP TABLE IF EXISTS ";


    /*
        TABLE:TIMELINE_TABLE Structure
     */

    protected static final String TIMELINE_TABLE = "TIMELINE";

    public static final class Timeline implements BaseColumns{

        public static final String TWEET_ID = "tweet_id";
        public static final String REPLY_ID = "reply_id";
        public static final String TWEET = "tweet";
//        public static final String USER_ID = "user_id";
        public static final String TIME = "tweettime";
        public static final String RT = "RT";
        public static final String FAV = "FAV";
    };

    protected static final String TIMELINE_CREATE =
        "CREATE TABLE "+ TIMELINE_TABLE +" (" +
            Timeline._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            Timeline.TWEET_ID + " LONG, " +
            Timeline.REPLY_ID + " LONG, " +
            Timeline.TWEET + " TEXT, " +
            Timeline.TIME + " INTEGER, " +
//            Timeline.USER_ID + " LONG " +
            Timeline.RT + " LONG, " +
            Timeline.FAV + " LONG " +
        ");";

    protected static final String TIMELINE_DROP = DROP_QUERY + TIMELINE_TABLE;

    protected static ContentValues CreateTimeLineData(Tweet tweet){
        ContentValues values = new ContentValues();

        values.put(Timeline.TWEET_ID, tweet.getId());
        values.put(Timeline.REPLY_ID, tweet.getReplyId());
        values.put(Timeline.TWEET, tweet.getTweetText());
        values.put(Timeline.TIME, tweet.getTime());
//        values.put(Timeline.USER_ID, tweet.getUser().getId());
        values.put(Timeline.RT, tweet.getRTcount());
        values.put(Timeline.FAV, tweet.getFAVcount());

        return values;
    }

    /*
        TABLE:USERS_TABLE Structure
     */

//    protected static final String USERS_TABLE = "Users";
   /* public static abstract class Users implements BaseColumns {

        public static final String USER_ID = "user_id";
        public static final String SCREENNAME = "name";
        public static final String PROFILEIMAGEURL = "profilepicURL";
    }

    protected static final String USERS_CREATE =
        "CREATE TABLE "+ USERS_TABLE +" (" +
            Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            Users.USER_ID + " LONG, " +
            Users.SCREENNAME + " TEXT, " +
            Users.PROFILEIMAGEURL + " TEXT " +
        ");";
*/
//    protected static final String USERS_DROP = DROP_QUERY + USERS_TABLE;

   /* protected static ContentValues CreateUsersData(TweetUser user){
        ContentValues values = new ContentValues();

        values.put(Users.USER_ID, user.getId());
        values.put(Users.SCREENNAME, user.getScreenName());
        values.put(Users.PROFILEIMAGEURL, user.getProfileImageURL().toString());

        return values;
    }*/
}
