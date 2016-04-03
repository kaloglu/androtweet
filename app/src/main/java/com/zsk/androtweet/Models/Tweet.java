package com.zsk.androtweet.Models;

import android.database.Cursor;
import java.util.Date;
import twitter4j.Status;

public class Tweet
{
  private int FAVcount;
  protected long Id;
  private int RTcount;
  protected long ReplyId;
  protected long Time;
  protected String TweetText;
  
  public Tweet(Cursor paramCursor)
  {
    setId(paramCursor.getLong(paramCursor.getColumnIndex("tweet_id")));
    setTweetText(paramCursor.getString(paramCursor.getColumnIndex("tweet")));
    setReplyId(paramCursor.getLong(paramCursor.getColumnIndex("reply_id")));
    setTime(paramCursor.getLong(paramCursor.getColumnIndex("tweettime")));
    setRTcount(paramCursor.getInt(paramCursor.getColumnIndex("RT")));
    setFAVcount(paramCursor.getInt(paramCursor.getColumnIndex("FAV")));
  }
  
  public Tweet(Status paramStatus)
  {
    setId(paramStatus.getId());
    setTweetText(paramStatus.getText());
    setReplyId(paramStatus.getInReplyToStatusId());
    setTime(paramStatus.getCreatedAt().getTime());
    setRTcount(paramStatus.getRetweetCount());
    setFAVcount(paramStatus.getFavoriteCount());
  }
  
  public int getFAVcount()
  {
    return this.FAVcount;
  }
  
  public long getId()
  {
    return this.Id;
  }
  
  public int getRTcount()
  {
    return this.RTcount;
  }
  
  public long getReplyId()
  {
    return this.ReplyId;
  }
  
  public long getTime()
  {
    return this.Time;
  }
  
  public String getTweetText()
  {
    return this.TweetText;
  }
  
  public void setFAVcount(int paramInt)
  {
    this.FAVcount = paramInt;
  }
  
  public void setId(long paramLong)
  {
    this.Id = paramLong;
  }
  
  public void setRTcount(int paramInt)
  {
    this.RTcount = paramInt;
  }
  
  public void setReplyId(long paramLong)
  {
    this.ReplyId = paramLong;
  }
  
  public void setTime(long paramLong)
  {
    this.Time = paramLong;
  }
  
  public void setTweetText(String paramString)
  {
    this.TweetText = paramString;
  }
}


/* Location:              /Users/kaloglu/Desktop/androtweet/dex2jar-2.0/classes-dex2jar.jar!/com/zsk/androtweet/Models/Tweet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */