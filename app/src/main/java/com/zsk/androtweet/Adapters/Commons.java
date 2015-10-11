package com.zsk.androtweet.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.zsk.androtweet.Database.DB_Model;
import com.zsk.androtweet.Models.Tweet;
import com.zsk.androtweet.R;

import java.util.List;
import java.util.Map;

import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class Commons {

    private static String TAG="ANDROTWEET";
    static RequestToken requestToken = null;
    static String oauth_url, oauth_verifier;
    static Dialog auth_dialog;
    static WebView web;
    static SharedPreferences pref_AndroTweet;
    static Twitter twitterObject;

    public static Twitter getTwitterObject() {

        ConfigurationBuilder builder = new ConfigurationBuilder();
        Twitter twitter;
        builder.setOAuthConsumerKey(pref_AndroTweet.getString("CONSUMER_KEY", ""));
        builder.setOAuthConsumerSecret(pref_AndroTweet.getString("CONSUMER_SECRET", ""));
        builder.setDebugEnabled(true).setJSONStoreEnabled(true);

        AccessToken accessToken = new AccessToken(pref_AndroTweet.getString("ACCESS_TOKEN", ""), pref_AndroTweet.getString("ACCESS_TOKEN_SECRET", ""));
        twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

        return twitter;
    }

    public static ProgressDialog getProgress(Context context) {
        return getProgress(context,"Progressing...");
    }
    public static ProgressDialog getProgress(Context context, String Title) {
        return getProgress(context,Title, DB_Model.HOLDED_REC_COUNT);
    }
    public static ProgressDialog getProgress(Context context, String Title,int MaxProgress) {
        return getProgress(context,Title,MaxProgress,ProgressDialog.STYLE_HORIZONTAL);
    }
    public static ProgressDialog getProgress(Context context, String Title,int MaxProgress,int progressStyle) {
        ProgressDialog progress = new ProgressDialog(context);

        progress.setTitle(Title);
        progress.setMax(MaxProgress);
        progress.setProgressStyle(progressStyle);
        progress.setIndeterminate(false);
        progress.setCanceledOnTouchOutside(false);

        return progress;
    }
    public static void ShowInfo(Context context,String Message){
        ShowInfo(context,Message,Toast.LENGTH_SHORT);
    }
    public static void ShowInfo(Context context, String Message, int duration) {
        Toast toast=Toast.makeText(context,Message,duration);

        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void logOut(Context context) {
        SharedPreferences.Editor edit = pref_AndroTweet.edit();
        edit.putString("ACCESS_TOKEN", "");
        edit.putString("ACCESS_TOKEN_SECRET", "");
        edit.commit();

        new DB_Model(context).drop();
        isLogon(context);
    }

    public static void log(String Message) {
        Log.e(TAG, Message);

    }

    static class TokenGet extends AsyncTask<String, String, String> {

        private Context context;
        Twitter twitter;
        public TokenGet(Context context) {
            this.context =context;

            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(pref_AndroTweet.getString("CONSUMER_KEY", ""), pref_AndroTweet.getString("CONSUMER_SECRET", ""));
        }
        @Override
        protected String doInBackground(String... args) {
            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return oauth_url;
        }

        @Override
        protected void onPostExecute(String oauth_url) {
            if(oauth_url != null){
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(context);
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                auth_dialog.setContentView(R.layout.auth_dialog);

                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && !authComplete){
                            authComplete = true;
                            Log.e("Url",url);
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");

                            auth_dialog.dismiss();
                            new AccessTokenGet(context,twitter).execute();
                        }else if(url.contains("denied")){
                            auth_dialog.dismiss();
                            ShowInfo(context, "Sorry !, Permission Denied");
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setCancelable(true);

            }else{
                ShowInfo(context, "Sorry !, Network Error or Invalid Credentials");
            }
        }
    }

    static class AccessTokenGet extends AsyncTask<String, String, Boolean> {

        private Context context;
        Twitter twitter;
        private ProgressDialog progress;

        public AccessTokenGet(Context context, Twitter twitter_object) {
            this.context =context;
            this.twitter=twitter_object;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = getProgress(context);
            progress.show();
        }



        @Override
        protected Boolean doInBackground(String... args) {

            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                SharedPreferences.Editor edit = pref_AndroTweet.edit();
                edit.putString("ACCESS_TOKEN", accessToken.getToken());
                edit.putString("ACCESS_TOKEN_SECRET",accessToken.getTokenSecret());

                edit.commit();

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (response) {
                progress.hide();
            }
        }

    }

    public static void isLogon(Context context) {

        if (pref_AndroTweet ==null){
            pref_AndroTweet = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = pref_AndroTweet.edit();
        String CONSUMER_KEY = "d2XSeF6fXPtVClaG3DALLjMT4";
        edit.putString("CONSUMER_KEY", CONSUMER_KEY);
        String CONSUMER_SECRET = "e9UJ6mQuLK0pkFl4OTghjjHp1NR6PqkbdqwIiwgEIR9zEXTbQU";
        edit.putString("CONSUMER_SECRET", CONSUMER_SECRET);
        edit.commit();

        if ((pref_AndroTweet.getString("ACCESS_TOKEN", "") != null) && !"".equals(pref_AndroTweet.getString("ACCESS_TOKEN", ""))) {
            return;
        }
        new TokenGet(context).execute();

        DB_Model DB = new DB_Model(context);
        DB.deleteOldTweets();
    }

    public static class PostTweet extends AsyncTask<String, String, String> {
        private Context context;
        private Dialog tDialog;
        private ProgressDialog progress;

        public PostTweet(Context context,Dialog tdialog) {
            this.context =context;
            this.tDialog=tdialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress= getProgress(context, "Posting tweet ...");
            progress.show();
        }

        protected String doInBackground(String... args) {

            try {
                twitter4j.Status response = getTwitterObject().updateStatus(args[0]);
                return response.toString();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(String res) {
            progress.dismiss();
            tDialog.dismiss();
            if (res != null) {
                ShowInfo(context, "Tweet Sucessfully Posted");
            } else {
                ShowInfo(context, "Error while tweeting !");
            }

        }
    }

    public static void deleteSelected(Context context, TweetAdapter theAdapter) {
//        DB_Model ATdb = new DB_Model(context);
        new DeleteTweets(theAdapter, context).execute();
    }

    private static class DeleteTweets extends AsyncTask<Void,Tweet,Boolean> {
        private DB_Model DB;
        private TweetAdapter theAdapter;
        private int selectedItems;
        private Context context;
        private ProgressDialog progress;

        public DeleteTweets(TweetAdapter Adapter, Context context) {
            theAdapter=Adapter;
            this.context=context;
            DB=new DB_Model(context);
            selectedItems=theAdapter.getSelectedCount();
            log(String.valueOf(selectedItems));
            twitterObject=getTwitterObject();
        }

        @Override
        protected void onPreExecute() {

            if(selectedItems>0) {
                progress = getProgress(context, "Deleting Selected Tweets...", selectedItems);
                progress.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isDone=false;
            int r=0;
            if (selectedItems<1)
                return false;
            for (int i=theAdapter.getCount()-1;i>=0;--i) {
                if (theAdapter.isSelectedPos[i]) {
                    r++;
                    try {
                        final Tweet tweet=theAdapter.getItem(i);
                        final int finalI = i;
                        final int finalR = r;
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.setProgress(finalR);
                                theAdapter.remove(tweet);
                                theAdapter.isSelectedPos[finalI] = false;

                            }
                        });
//                        twitterObject.getScreenName();
                        DB.deleteTweet(tweet);
                        twitterObject.destroyStatus(tweet.getId());

                        isDone=true;
                        Thread.sleep(500);
                    } catch (TwitterException e) {
                        log(e.getMessage());
                        e.printStackTrace();
                        return checkRateLimit();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return isDone;
        }

        private boolean checkRateLimit() {
            try {
                Map<String , RateLimitStatus> rateLimitStatus = getTwitterObject().getRateLimitStatus();
                for (final String endpoint : rateLimitStatus.keySet()) {
                    final RateLimitStatus status = rateLimitStatus.get(endpoint);
//                                log("Endpoint: " + endpoint);
                    if (status.getRemaining()==0)
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowInfo(context,endpoint + " limit exceeded! ("+status.getRemaining()+"/"+status.getLimit()+") \n"
                                        +"Please wait for " + (status.getSecondsUntilReset()/60) + " minute(s)");
                            }
                        });
//                                log(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
//                                log(" SecondsUntilReset: " + status.getSecondsUntilReset());
                }


            } catch (TwitterException e1) {
                e1.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            DB.deleteOldTweets();
            if (selectedItems>0)progress.dismiss();
            if (aBoolean){
                theAdapter.notifyDataSetChanged();
                ShowInfo(context, "Deleted all selected tweets!", Toast.LENGTH_LONG);
//                refreshTweetList((Activity) context,false);
            }

            super.onPostExecute(aBoolean);
            DB.close();
        }

    }

    public static void refreshTweetList(Activity context,ListView timeLine){
        refreshTweetList(context,timeLine,true);
    }
    public static void refreshTweetList(Activity context, ListView timeLine,boolean showProgressDialog) {
//        DB_Model DB= new DB_Model(context);

        new LoadTweets(context,showProgressDialog,timeLine).execute();



    }
    public static void refreshL(Context context, DB_Model DB, ListView timeLine) {
        List<Tweet> tweetList= DB.getTweetList();

        if (tweetList.size()>0){
            TweetAdapter TheAdapter = new TweetAdapter(context, R.layout.tweets_layout,tweetList);

            timeLine.setAdapter(TheAdapter);
        }else{
            timeLine.setAdapter(null);
        }
    }
    public static class LoadTweets extends AsyncTask<Void, Void, Boolean> {
        private final ListView timeLine;
        private DB_Model DB;
        private Paging p=new Paging();
        private ProgressDialog progress;
        private Boolean showProgressDialog;
        private Context context;
        int i=0;

        public LoadTweets(Context context, boolean showProgressDialog,ListView timeLine) {
            this.timeLine=timeLine;
            this.context=context;
            DB = new DB_Model(context);
            this.showProgressDialog=showProgressDialog;

        }

        @Override
        protected void onPreExecute() {
            p.setCount(DB_Model.HOLDED_REC_COUNT);
            if(showProgressDialog) {
                progress = getProgress(context, "Loading Tweets...", p.getCount(), ProgressDialog.STYLE_SPINNER);
                progress.show();
            }
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... voids) {
            try {
                DB.insertTweetList(getTwitterObject().getUserTimeline(p));
            } catch (Exception e) {
                log(e.getMessage());
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            DB.close();
            if(showProgressDialog) progress.dismiss();
            refreshL(context,DB, timeLine);
            super.onPostExecute(aBoolean);
        }

    }
}