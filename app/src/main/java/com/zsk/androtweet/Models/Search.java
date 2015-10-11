package com.zsk.androtweet.Models;

/**
 * Created by kaloglu on 17.08.2015.
 */
public class Search {
    private boolean viewMyTweets=true;
    private boolean viewMentions=true;
    private boolean viewRTs=true;
    private static Search instance=new Search();
    public long lastTweetId;

    private Search() {
        /**
         * This is SingleTon!
         */
    }

    public static Search getInstance() {
        return instance;
    }

    public static void setInstance(Search instance) {
        Search.instance = instance;
    }

    public boolean isViewMyTweets() {
        return viewMyTweets;
    }

    public void setViewMyTweets(boolean viewMyTweets) {
        this.viewMyTweets = viewMyTweets;
    }

    public boolean isViewMentions() {
        return viewMentions;
    }

    public void setViewMentions(boolean viewMentionss) {
        this.viewMentions = viewMentionss;
    }

    public boolean isViewRTs() {
        return viewRTs;
    }

    public void setViewRTs(boolean viewRTs) {
        this.viewRTs = viewRTs;
    }
}
