package com.zsk.androtweet.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.zsk.androtweet.Database.DB_Model;
import com.zsk.androtweet.Dialog.CustomDialog;
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
    private static String TAG = "ANDROTWEET";
    static Dialog auth_dialog;
    static String oauth_url;
    static String oauth_verifier;
    static SharedPreferences pref_AndroTweet;
    static RequestToken requestToken = null;
    static Twitter twitterObject;
    static WebView web;

    public static void ShowInfo(Context context, String msg) {
        ShowInfo(context, msg, Toast.LENGTH_SHORT);
    }

    public static void ShowInfo(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, duration);
        toast.setDuration(duration);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public static void deleteSelected(Context context, TweetAdapter theAdapter) {
        if ((theAdapter == null) || (theAdapter.getSelectedCount() == 0)) {
            Toast.makeText(context, "Please select tweets less one or more...", Toast.LENGTH_SHORT).show();
            return;
        }
        new DeleteTweets(theAdapter, context).execute();
    }

    public static ProgressDialog getProgress(Context context) {
        return getProgress(context, "Progressing...");
    }

    public static ProgressDialog getProgress(Context context, String title) {
        return getProgress(context, title, DB_Model.HOLDED_REC_COUNT);
    }

    public static ProgressDialog getProgress(Context context, String title, int maxProgress) {
        return getProgress(context, title, maxProgress, ProgressDialog.STYLE_HORIZONTAL);
    }

    public static ProgressDialog getProgress(Context context, String title, int maxProgress, int progressStyle) {
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setTitle(title);
        progressdialog.setMax(maxProgress);
        progressdialog.setProgressStyle(progressStyle);
        progressdialog.setIndeterminate(false);
        progressdialog.setCanceledOnTouchOutside(false);
        return progressdialog;
    }

    public static Twitter getTwitterObject() {
        ConfigurationBuilder localConfigurationBuilder = new ConfigurationBuilder();
        localConfigurationBuilder.setOAuthConsumerKey(pref_AndroTweet.getString("CONSUMER_KEY", ""));
        localConfigurationBuilder.setOAuthConsumerSecret(pref_AndroTweet.getString("CONSUMER_SECRET", ""));
        localConfigurationBuilder.setDebugEnabled(true).setJSONStoreEnabled(true);
        AccessToken localAccessToken = new AccessToken(pref_AndroTweet.getString("ACCESS_TOKEN", ""), pref_AndroTweet.getString("ACCESS_TOKEN_SECRET", ""));
        return new TwitterFactory(localConfigurationBuilder.build()).getInstance(localAccessToken);
    }

    public static void isLogon(Context context) {
        if (pref_AndroTweet == null) {
            pref_AndroTweet = context.getSharedPreferences(TAG, 0);
        }
        Editor localEditor = pref_AndroTweet.edit();
        localEditor.putString("CONSUMER_KEY", "d2XSeF6fXPtVClaG3DALLjMT4");
        localEditor.putString("CONSUMER_SECRET", "e9UJ6mQuLK0pkFl4OTghjjHp1NR6PqkbdqwIiwgEIR9zEXTbQU");
        localEditor.apply();
        if ((!"".equals(pref_AndroTweet.getString("ACCESS_TOKEN", "")))) {
            return;
        }
        new DB_Model(context).deleteOldTweets();
        new TokenGet(context).execute();
    }

    public static void log(String paramString) {
        Log.e(TAG, paramString);
    }

    public static void logOut(Context context) {
        Editor localEditor = pref_AndroTweet.edit();
        localEditor.putString("ACCESS_TOKEN", "");
        localEditor.putString("ACCESS_TOKEN_SECRET", "");
        localEditor.apply();
        new DB_Model(context).drop();
        isLogon(context);
    }

    public static void refreshL(Context context, DB_Model db_model, ListView timeLine) {
        List<Tweet> tweetList = db_model.getTweetList();

        if (tweetList.size() > 0) {
            TweetAdapter TheAdapter = new TweetAdapter(context, R.layout.tweets_layout, tweetList);

            timeLine.setAdapter(TheAdapter);
        } else {
            timeLine.setAdapter(null);
        }
    }

    public static void refreshTweetList(Activity activity) {
        refreshTweetList(activity, (ListView) activity.findViewById(R.id.tweetList_on_home));
    }

    public static void refreshTweetList(Activity activity, ListView timeLine) {
        refreshTweetList(activity, timeLine, true);
    }

    public static void refreshTweetList(Activity activity, ListView timeLine, boolean showProgressDialog) {
        new LoadTweets(activity, showProgressDialog, timeLine).execute();
        Toast.makeText(activity, "Refreshed...", Toast.LENGTH_SHORT).show();
    }


    static class AccessTokenGet extends AsyncTask<String, String, Boolean> {

        private Context context;
        Twitter twitter;
        private ProgressDialog progress;

        public AccessTokenGet(Context context, Twitter twitter_object) {
            this.context = context;
            this.twitter = twitter_object;
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
                edit.putString("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());
                edit.putString("userName", twitter.getScreenName()).apply();
                edit.apply();

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (response) {
                refreshTweetList((Activity) context);
                progress.hide();
            }
        }

    }

    private static class DeleteTweets extends AsyncTask<Void, Tweet, Boolean> {
        private DB_Model DB;
        private TweetAdapter theAdapter;
        private int selectedItems;
        private Context context;
        private ProgressDialog progress;

        public DeleteTweets(TweetAdapter Adapter, Context context) {
            theAdapter = Adapter;
            this.context = context;
            DB = new DB_Model(context);
            selectedItems = theAdapter.getSelectedCount();
            log(String.valueOf(selectedItems));
            twitterObject = getTwitterObject();
        }

        @Override
        protected void onPreExecute() {
            final CustomDialog customDialog = new CustomDialog(context, R.string.deleteTweets_title, R.string.deleteTweets_desc, 0, R.string.accept);
            customDialog.initOkButtonClickListener(new CustomDialog.okButtonClickListener() {
                @Override
                public void onClick() {
                    customDialog.dismiss();
                }
            });
            customDialog.initActionButtonClickListener(new CustomDialog.actionButtonClickListener() {
                @Override
                public void onClick() {
                    customDialog.dismiss();
                }
            });
            if (selectedItems > 0) {
                progress = getProgress(context, "Deleting Selected Tweets...", selectedItems);
                progress.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isDone = false;
            int r = 0;
            if (selectedItems < 1)
                return false;
            for (int i = theAdapter.getCount() - 1; i >= 0; --i) {
                if (theAdapter.isSelectedPos[i]) {
                    r++;
                    try {
                        final Tweet tweet = theAdapter.getItem(i);
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

                        isDone = true;
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
                Map<String, RateLimitStatus> rateLimitStatus = getTwitterObject().getRateLimitStatus();
                for (final String endpoint : rateLimitStatus.keySet()) {
                    final RateLimitStatus status = rateLimitStatus.get(endpoint);
//                                log("Endpoint: " + endpoint);
                    if (status.getRemaining() == 0)
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowInfo(context, endpoint + " limit exceeded! (" + status.getRemaining() + "/" + status.getLimit() + ") \n"
                                        + "Please wait for " + (status.getSecondsUntilReset() / 60) + " minute(s)");
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
            if (selectedItems > 0) progress.dismiss();
            if (aBoolean) {
                theAdapter.notifyDataSetChanged();

//                refreshTweetList((Activity) context,false);
            }

            super.onPostExecute(aBoolean);
            DB.close();
        }

    }

    public static class LoadTweets extends AsyncTask<Void, Void, Boolean> {
        private final ListView timeLine;
        private DB_Model DB;
        private Paging p = new Paging();
        private ProgressDialog progress;
        private Boolean showProgressDialog;
        private Context context;
        int i = 0;

        public LoadTweets(Context context, boolean showProgressDialog, ListView timeLine) {
            this.timeLine = timeLine;
            this.context = context;
            DB = new DB_Model(context);
            this.showProgressDialog = showProgressDialog;

        }

        @Override
        protected void onPreExecute() {
            p.setCount(DB_Model.HOLDED_REC_COUNT);
            if (showProgressDialog) {
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
            if (showProgressDialog) progress.dismiss();
            refreshL(context, DB, timeLine);
            super.onPostExecute(aBoolean);
        }

    }


    static class TokenGet extends AsyncTask<String, String, String> {


        private Context context;
        Twitter twitter;

        public TokenGet(Context context) {
            this.context = context;

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
            if (oauth_url != null) {
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(context);
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                auth_dialog.setContentView(R.layout.auth_dialog);

                web = (WebView) auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && !authComplete) {
                            authComplete = true;
                            Log.e("Url", url);
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");

                            auth_dialog.dismiss();
                            new AccessTokenGet(context, twitter).execute();
                        } else if (url.contains("denied")) {
                            auth_dialog.dismiss();
                            ShowInfo(context, "Sorry !, Permission Denied");
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setCancelable(true);

            } else {
                ShowInfo(context, "Sorry !, Network Error or Invalid Credentials");
            }
        }
    }
}