package com.zsk.androtweet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.zsk.androtweet.Adapters.Commons;
import com.zsk.androtweet.Adapters.TweetAdapter;
import com.zsk.androtweet.Database.DB_Model;
import com.zsk.androtweet.Dialog.CustomDialog;
import com.zsk.androtweet.Models.Search;
import com.zsk.androtweet.Models.Tweet;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Main
        extends Activity {
    private static final String TAG = "ANDROTWEET";
    TweetAdapter TheAdapter;
    private ListView tweetList;
    private CheckBox chk_All;
    private CheckBox chk_Mentions;
    private CheckBox chk_MyTweets;
    private CheckBox chk_RTs;
    private Context context;
    private LinearLayout lyt_selected;
    Search search;
    private static TextView txt_selected;
    private InterstitialAd mInterstitialAd;
    private int actionCount = 4;
    private int daysAgo = 4;

    private void init() {
        context = this;
        tweetList = ((ListView) findViewById(R.id.tweetList_on_home));

        chk_All = ((CheckBox) findViewById(R.id.chk_SelectAll));
        chk_MyTweets = (CheckBox) findViewById(R.id.chk_MyTweets);
        chk_Mentions = (CheckBox) findViewById(R.id.chk_MyTweets);
        chk_RTs = ((CheckBox) findViewById(R.id.chk_RTs));
        lyt_selected = ((LinearLayout) findViewById(R.id.lyt_selected));
        txt_selected = ((TextView) findViewById(R.id.txt_selectedCount));
    }

    private void init_Ads() {
        ((AdView) findViewById(R.id.adViewBanner)).loadAd(new AdRequest.Builder().build());

        try {
            new CheckSharing().execute();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public class CheckSharing extends AsyncTask<Void, Void, Boolean> {
        private final SharedPreferences pref_AndroTweet;
        private final Twitter twitterObject;
        String userName, tweetId;

        public CheckSharing() throws TwitterException {
            twitterObject = Commons.getTwitterObject();
            pref_AndroTweet = Main.this.getSharedPreferences(TAG, 0);
            userName = pref_AndroTweet.getString("userName", "");
            tweetId = pref_AndroTweet.getString(userName + "_sharedTweetID", "");

            if (userName.equals("")) {
                pref_AndroTweet.edit().putString("userName", twitterObject.getScreenName()).apply();
                userName = pref_AndroTweet.getString("userName", "");
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (userName.equals("") || tweetId.equals(""))
                return true;

            try {
                Tweet tweet = new Tweet(twitterObject.showStatus(Long.parseLong(tweetId)));
                daysAgo = (int) ((System.currentTimeMillis() - tweet.getTime()) / (1000 * 60 * 60 * 24));
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            if ((daysAgo < 4))
                return false;
            else
                return true;
        }

        @Override
        protected void onPostExecute(Boolean showInterstitial) {
            AndroTweetApp.setDaysAgo(daysAgo);
            AndroTweetApp.setUserName(userName);
            AndroTweetApp.setTweetId(tweetId);
            if (showInterstitial) {
                mInterstitialAd = new InterstitialAd(getBaseContext());
                mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        requestNewInterstitial();

                    }
                });

                requestNewInterstitial();

            }
            super.onPostExecute(showInterstitial);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void init_Listeners() {
        chk_All.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
                TheAdapter = ((TweetAdapter) tweetList.getAdapter());
                if (TheAdapter == null) {
                    try {
                        Commons.refreshTweetList((Activity) context, tweetList);
                        return;
                    } catch (Exception e) {
                        Commons.ShowInfo(context, "Please click to Refresh!");
                        return;
                    }
                }
                int i = 0;
                while (i < TheAdapter.getCount()) {
                    ((CheckBox) TheAdapter.getView(i, null, null).findViewById(R.id.chkTweet)).setChecked(paramAnonymousCompoundButton.isChecked());
                    TheAdapter.getView(i, null, (ViewGroup) findViewById(R.id.tweetList_on_home)).findViewById(R.id.chkTweet).callOnClick();
                    i += 1;
                }
                TheAdapter.notifyDataSetChanged();

            }
        });
        chk_RTs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
                search.setViewRTs(paramAnonymousBoolean);
                Commons.refreshL(Main.this, new DB_Model(Main.this), tweetList);
                chk_All.setChecked(false);
            }
        });
        chk_Mentions.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
                search.setViewMentions(paramAnonymousBoolean);
                Commons.refreshL(Main.this, new DB_Model(Main.this), tweetList);
                chk_All.setChecked(false);
            }
        });
        chk_MyTweets.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
                search.setViewMyTweets(paramAnonymousBoolean);
                Commons.refreshL(Main.this, new DB_Model(Main.this), tweetList);
                chk_All.setChecked(false);
            }
        });
    }

    public void doThings(View paramView) {
        if (TheAdapter == null) {
            TheAdapter = ((TweetAdapter) tweetList.getAdapter());
        }
        showAds();
        switch (paramView.getId()) {
            case R.id.deleteTweet:
                Commons.deleteSelected(this, TheAdapter);
                break;
            case R.id.refreshTweet:
                Commons.refreshTweetList(this, tweetList);
                break;
            case R.id.logOut:
                Commons.logOut(this);
                break;
            default:
                break;
        }
    }

    private void showAds() {
        actionCount += 1;
        if ((actionCount % 5) == 0) {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                final CustomDialog dialogScreen = new CustomDialog(context, R.string.no_more_ads_suggestions_title, R.string.no_more_ads_suggestions_desc, R.string.no_more_ads_rules, R.string.no_more_ads_actionButton);
                dialogScreen.initOkButtonClickListener(new CustomDialog.okButtonClickListener() {
                    @Override
                    public void onClick() {
                        dialogScreen.dismiss();
                        mInterstitialAd.show();
                    }
                });

                dialogScreen.initActionButtonClickListener(new CustomDialog.actionButtonClickListener() {
                    @Override
                    public void onClick() {
                        try {
                            new ShareApp().execute();
                        } catch (TwitterException e) {
                            e.printStackTrace();
                            Toast.makeText(Main.this, "Unexpected Error! Please Contact Me => AndroTweet1903@gmail.com", Toast.LENGTH_SHORT).show();
                        }
                        dialogScreen.dismiss();
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.home_timeline);
        Commons.isLogon(this);
        init_Ads();
        super.onCreate(savedInstanceState);
        search = Search.getInstance();

        init();
        init_Listeners();

        Commons.refreshTweetList(this, tweetList);

//        showAds();
    }

    public static void selectedCountChange(int isSelectedCount) {
        txt_selected.setText(String.valueOf(isSelectedCount));
    }

    public class ShareApp extends AsyncTask<Void, Void, Tweet> {
        private final SharedPreferences pref_AndroTweet;
        private final Twitter twitterObject;
        String userName;
        private Tweet tweet;

        public ShareApp() throws TwitterException {
            twitterObject = Commons.getTwitterObject();
            pref_AndroTweet = Main.this.getSharedPreferences(TAG, 0);
            userName = pref_AndroTweet.getString("userName", "");
        }

        @Override
        protected Tweet doInBackground(Void... params) {
            try {
                tweet = new Tweet(twitterObject.updateStatus("Cleaned my twitter profile! " +
                        "\n Removed tweets, mentions, and retweets by @AndroTweet1903 " +
                        "\n http://bit.ly/AndroTweet"));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return tweet;
        }

        @Override
        protected void onPostExecute(Tweet tweet) {
            SharedPreferences.Editor edit = pref_AndroTweet.edit();
            edit.putString(userName + "_sharedTweetID", String.valueOf(tweet.getId()));
            edit.apply();
            mInterstitialAd = null;
            super.onPostExecute(tweet);
        }
    }
}

