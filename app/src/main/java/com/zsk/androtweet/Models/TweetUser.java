package com.zsk.androtweet.Models;

import twitter4j.User;

public class TweetUser
{
  protected long Id;
  protected String ProfileImageURL;
  protected String ScreenName;
  
  public TweetUser() {}
  
  public TweetUser(User paramUser)
  {
    this.Id = paramUser.getId();
    this.ScreenName = paramUser.getScreenName();
    this.ProfileImageURL = paramUser.getProfileImageURL();
  }
  
  public long getId()
  {
    return this.Id;
  }
  
  public String getProfileImageURL()
  {
    return this.ProfileImageURL;
  }
  
  public String getScreenName()
  {
    return this.ScreenName;
  }
}


/* Location:              /Users/kaloglu/Desktop/androtweet/dex2jar-2.0/classes-dex2jar.jar!/com/zsk/androtweet/Models/TweetUser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */