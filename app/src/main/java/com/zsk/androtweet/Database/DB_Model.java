package com.zsk.androtweet.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zsk.androtweet.Models.Search;
import com.zsk.androtweet.Models.Tweet;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;

import static com.zsk.androtweet.Database.DB.CreateTimeLineData;
import static com.zsk.androtweet.Database.DB.TIMELINE_TABLE;
import static com.zsk.androtweet.Database.DB.Timeline;

public class DB_Model {
  public static final int HOLDED_REC_COUNT = 150;

  private static DBHelper dbhelper;
  private static SQLiteDatabase db;
  private long lastTweetid=0L;


  public DB_Model(Context context){
    if (dbhelper==null)
      dbhelper = new DBHelper(context);

    db = dbhelper.getWritableDatabase();
  }

  public void close() {
    db.close();
  }
  public void drop(){
    dbhelper.FreshDB(db);
  }

  public void insertTweetList(List<Status> statuses) {
    for (Status status : statuses){
      insertTweet(new Tweet(status));
    }
  }

  public void insertTweet(Tweet tweet) {
    if (checkTweet(tweet.getId())) {
      return;
    }
    Search.getInstance().lastTweetId=tweet.getId();
    db.insert(TIMELINE_TABLE, null, CreateTimeLineData(tweet));
  }
  public static boolean checkTweet(long tweet_id) {
    boolean checkTweet=false;
    String selectQuery = "SELECT  " + Timeline.TWEET_ID +
            " FROM " + TIMELINE_TABLE + " as TWEET  " +
            "WHERE TWEET." + Timeline.TWEET_ID + " = " + tweet_id;

//		log("QUERY: " + selectQuery);

    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor != null)
      checkTweet=(cursor.getCount()>0);

    return checkTweet;
  }

	/* (non-Javadoc)
	 * @see 
	 *
	 * (e.g. HOLDED_REC_COUNT = 100;)
	 */

  public List<Tweet> getTweetList() {
    return getTweetList(HOLDED_REC_COUNT);
  }

  public List<Tweet> getTweetList(int Rec_Count) {
    return getTweetList(null, Rec_Count);
  }

  public List<Tweet> getTweetList(String fromTime, int Rec_Count) {
    return getTweetList(fromTime, null,Rec_Count);
  }

  public List<Tweet> getTweetList(String fromTime, String toTime, int Rec_Count) {
    List<Tweet> tweets = new ArrayList<>();
    Search search=Search.getInstance();

    String query= "";
    if (search.isViewRTs())
      query += "SELECT * FROM " + TIMELINE_TABLE + " WHERE " + Timeline.TWEET + " like 'RT @%'";
    if (search.isViewRTs() && search.isViewMentions())
      query += " UNION ALL " ;
    if (search.isViewMentions())
      query += "SELECT * FROM " + TIMELINE_TABLE + " WHERE " + Timeline.TWEET + " like '@%'";
    if (search.isViewMyTweets() && (search.isViewMentions() || search.isViewRTs()))
      query += " UNION ALL " ;
    if (search.isViewMyTweets())
      query += "SELECT * FROM " + TIMELINE_TABLE + " WHERE " + Timeline.TWEET + " not like '@%' and " + Timeline.TWEET + " not like 'RT @%'";

    if (!query.equals("")) {
      String selectQuery = "select * FROM (" + query + ") as foo" +
              " ORDER BY " + Timeline.TIME + " DESC" +
              " LIMIT " + Rec_Count;

//            log("QUERY: " + selectQuery);
      Cursor cursor = db.rawQuery(selectQuery, null);

      // looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        do {
          Tweet tweet = new Tweet(cursor);
          // adding to todo list
          tweets.add(tweet);
        } while (cursor.moveToNext());
      }
    }
    return tweets;
  }

  public boolean deleteTweet(Tweet tweet) {
    String deleteQuery="delete from " + TIMELINE_TABLE +
            " WHERE " + Timeline.TWEET_ID + " = " + tweet.getId();
    db.execSQL(deleteQuery);
    return true;
  }

  public void deleteOldTweets() {
    String deleteQuery="delete from " + TIMELINE_TABLE;
    db.execSQL(deleteQuery);
  }
}