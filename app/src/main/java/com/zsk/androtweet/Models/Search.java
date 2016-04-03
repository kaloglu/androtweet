package com.zsk.androtweet.Models;

public class Search
{
  private static Search instance = new Search();
  public long lastTweetId;
  private boolean viewMentions = true;
  private boolean viewMyTweets = true;
  private boolean viewRTs = true;
  
  public static Search getInstance()
  {
    return instance;
  }
  
  public static void setInstance(Search paramSearch)
  {
    instance = paramSearch;
  }
  
  public boolean isViewMentions()
  {
    return this.viewMentions;
  }
  
  public boolean isViewMyTweets()
  {
    return this.viewMyTweets;
  }
  
  public boolean isViewRTs()
  {
    return this.viewRTs;
  }
  
  public void setViewMentions(boolean paramBoolean)
  {
    this.viewMentions = paramBoolean;
  }
  
  public void setViewMyTweets(boolean paramBoolean)
  {
    this.viewMyTweets = paramBoolean;
  }
  
  public void setViewRTs(boolean paramBoolean)
  {
    this.viewRTs = paramBoolean;
  }
}


/* Location:              /Users/kaloglu/Desktop/androtweet/dex2jar-2.0/classes-dex2jar.jar!/com/zsk/androtweet/Models/Search.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */