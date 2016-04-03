package com.zsk.androtweet.Database;

import android.content.ContentValues;
import android.provider.BaseColumns;
import com.zsk.androtweet.Models.Tweet;

public class DB
{
  protected static final String DBNAME = "AndroTweet";
  protected static final int DBVERSION = 7;
  protected static final String DROP_QUERY = "DROP TABLE IF EXISTS ";
  protected static final String TIMELINE_CREATE = "CREATE TABLE TIMELINE (_id INTEGER PRIMARY KEY AUTOINCREMENT , tweet_id LONG, reply_id LONG, tweet TEXT, tweettime INTEGER, RT LONG, FAV LONG );";
  protected static final String TIMELINE_DROP = "DROP TABLE IF EXISTS TIMELINE";
  protected static final String TIMELINE_TABLE = "TIMELINE";
  
  protected static ContentValues CreateTimeLineData(Tweet paramTweet)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("tweet_id", Long.valueOf(paramTweet.getId()));
    localContentValues.put("reply_id", Long.valueOf(paramTweet.getReplyId()));
    localContentValues.put("tweet", paramTweet.getTweetText());
    localContentValues.put("tweettime", Long.valueOf(paramTweet.getTime()));
    localContentValues.put("RT", Integer.valueOf(paramTweet.getRTcount()));
    localContentValues.put("FAV", Integer.valueOf(paramTweet.getFAVcount()));
    return localContentValues;
  }
  
  public static final class Timeline
    implements BaseColumns
  {
    public static final String FAV = "FAV";
    public static final String REPLY_ID = "reply_id";
    public static final String RT = "RT";
    public static final String TIME = "tweettime";
    public static final String TWEET = "tweet";
    public static final String TWEET_ID = "tweet_id";
  }
}


/* Location:              /Users/kaloglu/Desktop/androtweet/dex2jar-2.0/classes-dex2jar.jar!/com/zsk/androtweet/Database/DB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */