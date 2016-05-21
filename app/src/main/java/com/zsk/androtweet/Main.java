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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.zsk.androtweet.Adapters.Commons;
import com.zsk.androtweet.Adapters.TweetAdapter;
import com.zsk.androtweet.Database.DB_Model;
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
    private boolean showInterstitial = true;
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
            new checkSharing().execute();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public class checkSharing extends AsyncTask<Void, Void, Boolean> {
        private final SharedPreferences pref_AndroTweet;
        private final Twitter twitterObject;
        String userName, tweetId;
        private String tweetTime;

        public checkSharing() throws TwitterException {
            twitterObject = Commons.getTwitterObject();
            pref_AndroTweet = Main.this.getSharedPreferences(TAG, 0);
            userName = pref_AndroTweet.getString("userName", "");
            tweetId = pref_AndroTweet.getString(userName + "_sharedTweetID", "501045709710561281");

            if (userName.equals("")) {
                pref_AndroTweet.edit().putString("userName", twitterObject.getScreenName()).apply();
                userName = pref_AndroTweet.getString("userName", "");
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result;
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
            Main.this.showInterstitial = showInterstitial;

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
        actionCount += 1;
        if ((actionCount % 5) == 0) {
            if (mInterstitialAd!=null && mInterstitialAd.isLoaded())
                mInterstitialAd.show();
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = Search.getInstance();
        setContentView(R.layout.home_timeline);
        Commons.isLogon(this);

        init();
        init_Listeners();
        init_Ads();


        Commons.refreshTweetList(this, tweetList);
    }

    public static void selectedCountChange(int isSelectedCount) {
        txt_selected.setText(String.valueOf(isSelectedCount));
    }
}

