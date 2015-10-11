package com.zsk.androtweet.Models;

import android.database.Cursor;

import com.zsk.androtweet.Database.DB;

import twitter4j.Status;

public class Tweet {
    protected long Id;
    protected String TweetText;
    protected long ReplyId;
    protected long Time;
    private int RTcount;
    private int FAVcount;

    public Tweet(Status tweet) {
        setId(tweet.getId());
        setTweetText(tweet.getText());
        setReplyId(tweet.getInReplyToStatusId());
        setTime(tweet.getCreatedAt().getTime());
        setRTcount(tweet.getRetweetCount());
        setFAVcount(tweet.getFavoriteCount());
    }

    public Tweet(Cursor c) {
        setId(c.getLong(c.getColumnIndex(DB.Timeline.TWEET_ID)));
        setTweetText(c.getString(c.getColumnIndex(DB.Timeline.TWEET)));
        setReplyId(c.getLong(c.getColumnIndex(DB.Timeline.REPLY_ID)));
        setTime(c.getLong(c.getColumnIndex(DB.Timeline.TIME)));
        setRTcount(c.getInt(c.getColumnIndex(DB.Timeline.RT)));
        setFAVcount(c.getInt(c.getColumnIndex(DB.Timeline.FAV)));
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTweetText() {
        return TweetText;
    }

    public void setTweetText(String tweetText) {
        TweetText = tweetText;
    }

    public long getReplyId() {
        return ReplyId;
    }

    public void setReplyId(long replyId) {
        ReplyId = replyId;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public int getRTcount() {
        return RTcount;
    }

    public void setRTcount(int RTcount) {
        this.RTcount = RTcount;
    }

    public int getFAVcount() {
        return FAVcount;
    }

    public void setFAVcount(int FAVcount) {
        this.FAVcount = FAVcount;
    }

}
