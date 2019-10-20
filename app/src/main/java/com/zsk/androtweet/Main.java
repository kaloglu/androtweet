package com.zsk.androtweet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.zsk.androtweet.Adapters.Commons;
import com.zsk.androtweet.Adapters.TweetAdapter;
import com.zsk.androtweet.Database.DB_Model;
import com.zsk.androtweet.Models.Search;

public class Main
        extends Activity {
    TweetAdapter TheAdapter;
    private ListView tweetList;
    private CheckBox chk_All;
    private CheckBox chk_Mentions;
    private CheckBox chk_MyTweets;
    private CheckBox chk_RTs;
    private Context context;
    Search search;
    private static TextView txt_selected;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;

    private void init() {
        context = this;
        tweetList = findViewById(R.id.tweetList_on_home);

        chk_All = findViewById(R.id.chk_SelectAll);
        chk_MyTweets = findViewById(R.id.chk_MyTweets);
        chk_Mentions = findViewById(R.id.chk_Mentions);
        chk_RTs = findViewById(R.id.chk_RTs);
        txt_selected = findViewById(R.id.txt_selectedCount);
    }

    public void createAndLoadRewardedAd() {
        rewardedAd = new RewardedAd(this, getResources().getString(R.string.rewarded_ad_unit_id));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Log.e("REWARDED_VIDEO", "LOADED!");
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                Log.e("REWARDED_VIDEO", "FAILED!");
                createAndLoadInterstitialAd();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    public void createAndLoadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e("INTERSTITIAL_AD", "LOADED!");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("INTERSTITIAL_AD", "FAILED!");
                createAndLoadRewardedAd();
            }

            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                createAndLoadRewardedAd();
            }

        });
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void init_Ads() {
        ((AdView) findViewById(R.id.adViewBanner)).loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createAndLoadRewardedAd();
            }
        });
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
        rewardedAd.show(this, new RewardedAdCallback() {

            @Override
            public void onRewardedAdFailedToShow(int i) {
                super.onRewardedAdFailedToShow(i);
                mInterstitialAd.show();
            }

            @Override
            public void onRewardedAdClosed() {
                Log.e("REWARDED_VIDEO", "VIDEO_CLOSED!");
                createAndLoadRewardedAd();
            }

            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                Log.e("REWARDED_VIDEO", "REWARD!");
            }
        });
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

}

